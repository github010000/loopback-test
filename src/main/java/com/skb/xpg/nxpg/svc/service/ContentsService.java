package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
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
	public void getSynopsisContents(Map<String, Object> rtn, Map<String, String> param) throws Exception {
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

			Map<String, Object> mapping = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO, epsd_rslu_id));
			sris_id = mapping.get("sris_id") + "";
			epsd_id = mapping.get("epsd_id") + "";
		} 
		
		epsd = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id));
		if (epsd != null) {
			sris_id = epsd.get("sris_id") + "";
		}
		if (sris_id != null && !sris_id.isEmpty()) {
			sris = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, sris_id));
		}
		
		if (sris != null) {
			if ("Y".equals(param.get("yn_recent")) && sris != null && "01".equals(sris.get("sris_typ_cd"))) {

				String series = redisClient.hget(NXPGCommon.SYNOPSIS_SRIS, sris_id);
				if (series != null && !series.isEmpty()) {
					String last_epsd_id = "";
					Pattern p = Pattern.compile(".*epsd_id\":\"([^\"]+)\"");
					Matcher m = p.matcher(series);
					
					if (m.find()) {
//							System.out.println(m.group(1));
						last_epsd_id = m.group(1);
					}
					
					if (!last_epsd_id.isEmpty()) {
						epsd_id = last_epsd_id;
					}
				}
			}
		}
		

		// 조회값 없음
		if (epsd == null) {
			rtn.put("result", "9998");
		} else {
			sris.putAll(epsd);
			
			sris.remove("relation_contents");
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
			sris.put("session_id",  CastUtil.getString(param.get("session_id")));
			sris.put("track_id", CastUtil.getString(param.get("track_id")));
			rtn.put("result", "0000");
			rtn.put("contents", sris);

			Map<String, Object> purchares = getContentsPurchares(sris_id);
			if (purchares != null && purchares.get("products") != null) {
				List<Map<String, Object>> products = CastUtil.getObjectToMapList(purchares.get("products"));
				DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", true);
				rtn.put("purchares", purchares.get("products"));
			}
		}
	}
	
	//IF-NXPG-010
	public Map<String, Object> getContentsCommerce(String ver, Map<String, String> param) throws Exception {
		Map<String, Object> commerce = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_COMMERCE, param.get("sris_id")));
		
		if (commerce.containsKey("info_id"))
			commerce.put("info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("info_id").toString())));
		if (commerce.containsKey("delivery_info_id"))
			commerce.put("delivery_info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("delivery_info_id").toString())));
		if (commerce.containsKey("refund_info_id"))
			commerce.put("refund_info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("refund_info_id").toString())));
		if (commerce.containsKey("clause_info_id"))
			commerce.put("clause_info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("clause_info_id").toString())));
		return commerce;
	}
	
	//IF-NXPG-011 콘텐츠용 인물정보 불러오기 - 아래 메소드와 같이 수정 필요
	public void getContentsPeople(Map<String, Object> contents, String epsd_id) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, epsd_id);
		if (redisData != null && !"".equals(redisData)) {
			contents.putAll(CastUtil.StringToJsonMap(redisData));
		} else {
			contents.put("peoples", null);
		}
	}
	
	//IF-NXPG-011
	public Map<String, Object> getContentsPeople(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, param.get("epsd_id")));
	}
	
	//IF-NXPG-012
	public Map<String, Object> getContentsPreview(Map<String, Object> contents, String sris_id) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, sris_id);
		if (redisData != null && !"".equals(redisData)) {
			contents.putAll(CastUtil.StringToJsonMap(redisData));
		} else {
			contents.put("preview", null);
		}
		
//			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, param.get("sris_id")));
		return contents;
	}	
	
	//IF-NXPG-025
	public Object getContentsSeries(String sris_id) throws Exception {
		Map<String, Object> temp = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRIS, sris_id));
		if (temp != null && temp.get("episodes") != null) {
			return temp.get("episodes");
		}
		return null;
	}	
	
	//IF-NXPG-013
	public void getContentsCorner(Map<String, Object> contents, String epsd_id) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_CORNER, epsd_id);
		Map <String, Object> redisMap = null;
		if(redisData!=null&&!"".equals(redisData)) {
			redisMap = CastUtil.StringToJsonMap(redisData);
			contents.putAll(redisMap);
		}else {
			contents.put("corners", null);
		}
	}	
	
	//IF-NXPG-013
	public Map<String, Object> getCornerGather(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_GATHER, param.get("cnr_grp_id")));
	}	
	
	//IF-NXPG-014
	public Map<String, Object> getContentsGwsynop(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_GATEWAY, param.get("sris_id")));
	}
	
	//IF-NXPG-015
	public Map<String, Object> getContentsPurchares(String sris_id) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PURCHARES, sris_id));
	}
	
	//IF-NXPG-017
	public Map<String, Object> getContentsVodList(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_VODLIST, param.get("prs_id")));
	}

	//IF-NXPG-01?
	public Map<String, Object> getContentsReview(String ver, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_REVIEW, param.get("sris_id"));
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
	}

	//IF-NXPG-01?
	public void getContentsReview(Map<String, Object> contents, String sris_id) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_REVIEW, sris_id);
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
	}
	
	//IF-NXPG-01?
	public Map<String, Object> getPeopleInfo(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.PEOPLE_INFO, param.get("prs_id")));
	}
}
