package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;

@Service
public class ContentsService {

	@Autowired
	private RedisClient redisClient;
	
//	@Autowired
//	private Properties properties;
	
	//IF-NXPG-007
	public void getSynopsisContents(Map<String, Object> rtn, Map<String, String> param) {
		try {
			String sris_id = "", epsd_id = "", epsd_rslu_id = "";
			Map<String, Object> sris = null;
			Map<String, Object> epsd = null;
			if ("3".equals(param.get("search_type"))) {
				
				sris_id = param.get("sris_id");
				epsd_id = param.get("epsd_id");
				
				
			} else if ("1".equals(param.get("search_type"))) {

				epsd_id = param.get("epsd_id");
				
			} else if ("2".equals(param.get("search_type"))) {

				epsd_rslu_id = param.get("epsd_rslu_id");

				Map<String, Object> mapping = CastUtil.StringToJsonMap((String) redisClient.hget("contents_cidinfo", epsd_rslu_id));
				sris_id = mapping.get("sris_id") + "";
				epsd_id = mapping.get("epsd_id") + "";
			}
			
			epsd = CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id));
			if (sris_id != null && sris_id.isEmpty()) {
				sris_id = epsd.get("sris_id") + "";
			}
			sris = CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, sris_id));

			if ("Y".equals(param.get("yn_recent")) && sris != null && "01".equals(sris.get("sris_typ_cd"))) {

				String series = (String) redisClient.hget("synopsis_sris", sris_id);
				if (series != null && !series.isEmpty()) {
					String last_epsd_id = "";
					Pattern p = Pattern.compile(".*epsd_id\":\"([^\"]+)\"");
					Matcher m = p.matcher(series);
					
					if (m.find()) {
						System.out.println(m.group(1));
						last_epsd_id = m.group(1);
					}
					
					if (!last_epsd_id.isEmpty()) {
						epsd_id = last_epsd_id;
					}
				}
			}

			// 조회값 없음
			if (sris == null) {
				rtn.put("result", "9998");
			} else {
				sris.putAll(epsd);
				// 성공
				getContentsPeople(sris, epsd_id);
				getContentsCorner(sris, epsd_id);
				getContentsPreview(sris, sris_id);
				getContentsReview(sris, sris_id);

				sris.put("combine_product_yn", "N");
				if (param.containsKey("ukey_prd_id")) {
					List<Map<String, Object>> ukeyList = CastUtil.getObjectToMapList(sris.get("ukey_products"));
					for (Map<String, Object> ukey : ukeyList) {
						if (ukey.containsKey("ukey_prd_id")) {
							if (ukey.get("ukey_prd_id").equals(param.get("ukey_prd_id"))) {
								sris.put("combine_product_yn", "Y");
							}
						}
					}
				}
				
				sris.put("series_info", getContentsSeries(sris_id));
				rtn.put("result", "0000");
				rtn.put("contents", sris);

				Map<String, Object> purchares = getContentsPurchares(sris_id);
				if (purchares != null && purchares.get("products") != null) {
					List<Map<String, Object>> products = CastUtil.getObjectToMapList(purchares.get("products"));
					DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", true);
					rtn.put("purchares", purchares.get("products"));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//IF-NXPG-010
	public Map<String, Object> getContentsCommerce(String ver, Map<String, String> param) {
		try {
			Map<String, Object> commerce = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_commerce", param.get("sris_id")));
			
			commerce.put("info_id", CastUtil.StringToJsonMap((String) redisClient.hget("contents_phrase", commerce.get("info_id").toString())));
			commerce.put("delivery_info_id", CastUtil.StringToJsonMap((String) redisClient.hget("contents_phrase", commerce.get("delivery_info_id").toString())));
			commerce.put("refund_info_id", CastUtil.StringToJsonMap((String) redisClient.hget("contents_phrase", commerce.get("refund_info_id").toString())));
			return commerce;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//IF-NXPG-011 콘텐츠용 인물정보 불러오기 - 아래 메소드와 같이 수정 필요
	public void getContentsPeople(Map<String, Object> contents, String epsd_id) {
		try {
			String redisData = (String) redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, epsd_id);
			if (redisData != null && !"".equals(redisData)) {
				contents.putAll(CastUtil.StringToJsonMap(redisData));
			} else {
				contents.put("peoples", null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//IF-NXPG-011
	public Map<String, Object> getContentsPeople(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, param.get("epsd_id")));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//IF-NXPG-012
	public Map<String, Object> getContentsPreview(Map<String, Object> contents, String sris_id) {
		try {
			String redisData = (String) redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, sris_id);
			if (redisData != null && !"".equals(redisData)) {
				contents.putAll(CastUtil.StringToJsonMap(redisData));
			} else {
				contents.put("preview", null);
			}
			
//			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, param.get("sris_id")));
			return contents;
		} catch (Exception e) {
			return null;
		}
	}	
	
	//IF-NXPG-025
	public Object getContentsSeries(String sris_id) {
		try {
			Map<String, Object> temp = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_sris", sris_id));
			return temp.get("episodes");
		} catch (Exception e) {
			return null;
		}
	}	
	
	//IF-NXPG-013
	public void getContentsCorner(Map<String, Object> contents, String epsd_id) {
		try {
			String redisData = (String) redisClient.hget("contents_corner", epsd_id);
			Map <String, Object> redisMap = null;
			if(redisData!=null&&!"".equals(redisData)) {
				redisMap = CastUtil.StringToJsonMap(redisData);
				contents.putAll(redisMap);
			}else {
				contents.put("corners", null);
			}
			
//			return CastUtil.StringToJsonMap((String) redisClient.hget("contents_corner", param.get("epsd_id")));
		} catch (Exception e) {
		}
	}	
	
	//IF-NXPG-013
	public Map<String, Object> getCornerGather(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("corner_gather", param.get("cnr_grp_id")));
		} catch (Exception e) {
			return null;
		}
	}	
	
	//IF-NXPG-014
	public List getContentsGwsynop(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonList((String) redisClient.hget("synopsis_gateway", param.get("sris_id")));
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	//IF-NXPG-015
	public Map<String, Object> getContentsPurchares(String sris_id) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PURCHARES, sris_id));
		} catch (Exception e) {
			return null;
		}
	}
	
	//IF-NXPG-017
	public Map<String, Object> getContentsVodList(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_VODLIST, param.get("prs_id")));
		} catch (Exception e) {
			return null;
		}
	}

	//IF-NXPG-01?
	public Map<String, Object> getContentsReview(String ver, Map<String, String> param) {
		
		try {
			String redisData = (String) redisClient.hget("contents_review", param.get("sris_id"));
			Map<String, Object> root = null;
			if (redisData != null && !"".equals(redisData)) {
				root = CastUtil.StringToJsonMap(redisData);
				
				int idx = 1;
				int start = CastUtil.getStringToInteger(param.get("page_no"));
				if (start < 1) {
					start = 1;
				}
				int count = CastUtil.getStringToInteger(param.get("page_cnt"));
				// Map<String, Object> menus = CastUtil.getObjectToMap(root.get("menus"));
				List<Map<String, Object>> sites = CastUtil.getObjectToMapList(root.get("sites"));
				Map<String, Object> useSite = null;
				
				for (Map<String, Object> site : sites) {
					if (site.get("site_cd").equals(param.get("site_cd"))) {
						List<Map<String, Object>> reviews = CastUtil.getObjectToMapList(site.get("reviews"));
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						if (reviews != null && reviews.size() > 0) {
							for (Map<String, Object> review : reviews) {
								
								if (review != null && start <= idx && idx <= (start + count - 1)) {
									list.add(review);
								}
								idx++;
							}
						}
						site.put("reviews", list);
						useSite = site;
					}
					
					
				}
				root.put("sites", useSite);
			}
			return root;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//IF-NXPG-01?
	public void getContentsReview(Map<String, Object> contents, String sris_id) {
		try {
			String redisData = (String) redisClient.hget("contents_review", sris_id);
			Map<String, Object> root = null;
			if (redisData != null && !"".equals(redisData)) {
				root = CastUtil.StringToJsonMap(redisData);

				Map<String, Object> temp = null;
				// Map<String, Object> menus = CastUtil.getObjectToMap(root.get("menus"));
				List<Map<String, Object>> sites = CastUtil.getObjectToMapList(root.get("sites"));

				for (Map<String, Object> site : sites) {
					temp = null;
					List<Map<String, Object>> reviews = CastUtil.getObjectToMapList(site.get("reviews"));
					if (reviews != null && reviews.size() > 0) {
						temp = reviews.get(0);
					}
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					if (temp != null) {
						list.add(temp);
					}
					site.put("reviews", list);
				}

				contents.put("site_review", root);
			} else {
				contents.put("site_review", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//IF-NXPG-01?
	public Map<String, Object> getPeopleInfo(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("people_info", param.get("prs_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
