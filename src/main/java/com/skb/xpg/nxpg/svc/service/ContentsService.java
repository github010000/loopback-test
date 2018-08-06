package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.skb.xpg.nxpg.svc.util.CiModeUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;

@Service
public class ContentsService {

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private Properties properties;
	
	//IF-NXPG-007
	public void getSynopsisContents(Map<String, Object> rtn, Map<String, String> param) throws Exception {
		String sris_id = "", epsd_id = "", epsd_rslu_id = "";
		Map<String, Object> sris = null;
		Map<String, Object> epsd = null;
		String rslu_type = CastUtil.getString(param.get("rslu_type"));
		Map<String, String> checkdate = properties.getCheckdate();
		
		if ("3".equals(param.get("search_type"))) {
			
			sris_id = param.get("sris_id");
			epsd_id = param.get("epsd_id");
			
			
		} else if ("1".equals(param.get("search_type"))) {

			epsd_id = param.get("epsd_id");
			
		} else if ("2".equals(param.get("search_type"))) {

			epsd_rslu_id = param.get("epsd_rslu_id");

			Map<String, Object> mapping = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO, epsd_rslu_id, param));
			if (mapping != null) {
				sris_id = mapping.get("sris_id") + "";
				epsd_id = mapping.get("epsd_id") + "";
			} else {
				rtn.put("result", "9998");
				return;
			}
		} 
		
		epsd = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id, param));
		if (epsd != null) {
			sris_id = epsd.get("sris_id") + "";
		}
		if (sris_id != null && !sris_id.isEmpty()) {
			sris = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, sris_id, param));
		}
		
		if (sris != null) {
			if ("Y".equals(param.get("yn_recent")) && sris != null && "01".equals(sris.get("sris_typ_cd"))) {

				String series = redisClient.hget(NXPGCommon.SYNOPSIS_SRIS, sris_id, param);
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
						epsd = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id, param));
					}
				}
			}
			List<Map<String, Object>> epsd_rslu_info = CastUtil.getObjectToMapList(epsd.get("epsd_rslu_info"));
			
			for (Iterator<Map<String,Object>> iterator = epsd_rslu_info.iterator(); iterator.hasNext() ; ) {
				Map<String, Object> map = CastUtil.getObjectToMap(iterator.next());
				String rslu_typ_cd = CastUtil.getObjectToString(map.get("rslu_typ_cd"));
				
				//진입한 STB의 화질이 콘텐츠의 화질보다 낮을 경우 필터링.(상위 화질은 안보이게 한다.)
				if (rslu_type != null && !rslu_type.isEmpty()
						&& CastUtil.getStringToInteger(rslu_typ_cd) > CastUtil.getStringToInteger(rslu_type)) {
					iterator.remove();
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
			getContentsPeople(sris, epsd_id, param);
			getContentsCorner(sris, epsd_id, param);
			getContentsPreview(sris, sris_id, param);
			getContentsReview(sris, sris_id, param);

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

			sris.put("series_info", getContentsSeries(sris_id, param));
			sris.put("session_id",  CastUtil.getString(param.get("session_id")));
			sris.put("track_id", CastUtil.getString(param.get("track_id")));
			sris.put("menu_id", CastUtil.getString(param.get("cur_menu")));
			sris.put("cw_call_id", CastUtil.getString(param.get("cw_call_id")));
			
			String tempEnding = redisClient.hget("ending_cwcallidval", "ending_cwcallidval", param);
			Map<String, Object> tempEndingMap = CastUtil.StringToJsonMap(tempEnding);
			sris.put("ending_cw_call_id_val", null);
			if (tempEndingMap != null && tempEndingMap.get("cw_call_id_val") != null) {
				sris.put("ending_cw_call_id_val", tempEndingMap.get("cw_call_id_val"));
			}
			
			// products 날짜 체크
			if (sris != null) {
				List<Map<String, Object>> products = CastUtil.getObjectToMapList(sris.get("products"));
				DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", false);
				if (products != null) {
					for (int i = 0; i < products.size(); i++) {
						Map<String, Object> product = products.get(i);
						String strTemp = null;
						
						// prd_prc_to_dt
						if (product.get("prd_prc_to_dt") != null)
							strTemp = product.get("prd_prc_to_dt").toString();
						if (DateUtil.checkDate(strTemp, CastUtil.getStringToInteger(checkdate.get("prdprctodt"))*-1))
							product.put("expire_prd_prc_dt", strTemp);
						else 
							product.put("expire_prd_prc_dt", "");
						
						// next_prd_prc_fr_dt
						if (product.get("next_prd_prc_fr_dt") != null)
							strTemp = product.get("next_prd_prc_fr_dt").toString();
						if (!DateUtil.checkDate(strTemp, CastUtil.getStringToInteger(checkdate.get("nextprdprcfrdt"))))
							product.put("next_prd_prc_fr_dt", "");
					}
				}
			}
			//////////////////////////
			
			rtn.put("result", "0000");
			rtn.put("contents", sris);

			Map<String, Object> purchares = getContentsPurchares(sris_id, param);
			rtn.put("purchares", new ArrayList());
			if (purchares != null && purchares.get("products") != null) {
				List<Map<String, Object>> products = CastUtil.getObjectToMapList(purchares.get("products"));
				DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", false);
				if(NXPGCommon.isCIMode()) {
					products = CiModeUtil.getPrdFilter(products);
//					rtn.put("purchares", products);
				} else {
					products = CastUtil.getObjectToMapList(purchares.get("products"));
//					rtn.put("purchares", purchares.get("products"));
				}
				
				// purchares 날짜 체크
				for(int i = 0; i < products.size(); i++) {
					Map<String, Object> product = products.get(i);
					String strTemp = null;
					
					// 테스트 코드 
//					product.put("ppm_orgnz_fr_dt", "20180711000000");
//					product.put("ppm_orgnz_to_dt", "20180718000000");
//					product.put("prd_prc_to_dt", "20180812000000");
//					product.put("next_prd_prc_fr_dt", "20180710000000");

					// prd_prc_to_dt
					if (product.get("prd_prc_to_dt") != null)
						strTemp = product.get("prd_prc_to_dt").toString();
					if (DateUtil.checkDate(strTemp, CastUtil.getStringToInteger(checkdate.get("prdprctodt"))*-1))
						product.put("expire_prd_prc_dt", strTemp);
					else 
						product.put("expire_prd_prc_dt", "");

					// next_prd_prc_fr_dt
					if (product.get("next_prd_prc_fr_dt") != null)
						strTemp = product.get("next_prd_prc_fr_dt").toString();
					if (!DateUtil.checkDate(strTemp, CastUtil.getStringToInteger(checkdate.get("nextprdprcfrdt"))))
						product.put("next_prd_prc_fr_dt", "");
					
					// ppm_orgnz_fr_dt
					if (product.get("ppm_orgnz_fr_dt") != null)
						strTemp = product.get("ppm_orgnz_fr_dt").toString();
					if (!DateUtil.checkDate(strTemp, CastUtil.getStringToInteger(checkdate.get("ppmorgnzfrdt"))))
						product.put("ppm_orgnz_fr_dt", "");

					// ppm_orgnz_to_dt
					if (product.get("ppm_orgnz_to_dt") != null)
						strTemp = product.get("ppm_orgnz_to_dt").toString();
					if (!DateUtil.checkDate(strTemp, CastUtil.getStringToInteger(checkdate.get("ppmorgnzfrdt"))))
						product.put("ppm_orgnz_to_dt", "");
				}
				///////////////////////
				
				rtn.put("purchares", products);
			}
		}
	}
	
	//IF-NXPG-010
	public Map<String, Object> getContentsCommerce(String ver, Map<String, String> param) throws Exception {
		Map<String, Object> commerce = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_COMMERCE, param.get("sris_id"), param));
		
		if (commerce.containsKey("info_id"))
			commerce.put("info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("info_id").toString(), param)));
		if (commerce.containsKey("delivery_info_id"))
			commerce.put("delivery_info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("delivery_info_id").toString(), param)));
		if (commerce.containsKey("refund_info_id"))
			commerce.put("refund_info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("refund_info_id").toString(), param)));
		if (commerce.containsKey("clause_info_id"))
			commerce.put("clause_info_id", CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PHRASE, commerce.get("clause_info_id").toString(), param)));
		return commerce;
	}
	
	//IF-NXPG-011 콘텐츠용 인물정보 불러오기 - 아래 메소드와 같이 수정 필요
	public void getContentsPeople(Map<String, Object> contents, String epsd_id, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, epsd_id, param);
		if (redisData != null && !"".equals(redisData)) {
			contents.putAll(CastUtil.StringToJsonMap(redisData));
		} else {
			contents.put("peoples", null);
		}
	}
	
	//IF-NXPG-011
	public Map<String, Object> getContentsPeople(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, param.get("epsd_id"), param));
	}
	
	//IF-NXPG-012
	public Map<String, Object> getContentsPreview(Map<String, Object> contents, String sris_id, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, sris_id, param);
		if (redisData != null && !"".equals(redisData)) {
			contents.putAll(CastUtil.StringToJsonMap(redisData));
		} else {
			contents.put("preview", null);
		}
		
//			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, param.get("sris_id")));
		return contents;
	}	
	
	//IF-NXPG-025
	public Object getContentsSeries(String sris_id, Map<String, String> param) throws Exception {
		Map<String, Object> temp = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRIS, sris_id, param));
		if (temp != null && temp.get("episodes") != null) {
			return temp.get("episodes");
		}
		return null;
	}	
	
	//IF-NXPG-013
	public void getContentsCorner(Map<String, Object> contents, String epsd_id, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_CORNER, epsd_id, param);
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
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_GATHER, param.get("cnr_grp_id"), param));
	}	
	
	//IF-NXPG-014
	public Map<String, Object> getContentsGwsynop(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_GATEWAY, param.get("sris_id"), param));
	}
	
	//IF-NXPG-015
	public Map<String, Object> getContentsPurchares(String sris_id, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PURCHARES, sris_id, param));
	}
	
	//IF-NXPG-017
	public Map<String, Object> getContentsVodList(String ver, Map<String, String> param) throws Exception {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_VODLIST, param.get("prs_id"), param));
	}

	//IF-NXPG-01?
	public Map<String, Object> getContentsReview(String ver, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_REVIEW, param.get("sris_id"), param);
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
	public void getContentsReview(Map<String, Object> contents, String sris_id, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.CONTENTS_REVIEW, sris_id, param);
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
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.PEOPLE_INFO, param.get("prs_id"), param));
	}
}
