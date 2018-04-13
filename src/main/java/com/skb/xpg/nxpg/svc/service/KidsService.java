package com.skb.xpg.nxpg.svc.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;

@Service
public class KidsService {

	@Autowired
	private RedisClient redisClient;
	@Autowired
	ContentsService contentsService;
	
	// IF-NXPG-101
	public List getMenuKzchar(String ver, Map<String, String> param) {
		try {
			List<Object> kzchar = CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsGnb", param.get("menu_stb_svc_id")));
			DateUtil.getCompareObject(kzchar, "dist_fr_dt", "dist_to_dt", false);
			
			return CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsCharacter", param.get("menu_stb_svc_id")));
		} catch (Exception e) {
			return null;
		}
	}
	// IF-NXPG-102
	public void getMenuKzgnb(Map<String, Object> rtn, Map<String, String> param) {
		try {
			List<Object> kzgnb = CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsGnb", param.get("menu_stb_svc_id")));
			
			String version = StringUtils.defaultIfEmpty((String) redisClient.hget("version", "menu_kidsGnb"), "");
			
			if (kzgnb == null) {
				rtn.put("result", "9998");
			}
			// 성공
			else {
				DateUtil.getCompareObject(kzgnb, "dist_fr_dt", "dist_to_dt", false);
				if (param.containsKey("version") && !param.get("version").isEmpty() && param.get("version").compareTo(version) >= 0) {
					rtn.put("reason", "최신버전");
				}
				rtn.put("result", "0000");
				rtn.put("menus", kzgnb);
				// 카운트 넣어주기
				if (kzgnb != null)
					rtn.put("total_count", kzgnb.size());
			}
			
		} catch (Exception e) {
		}
	}
	// IF-NXPG-401
	public String getMenulfthomemapping(String ver, Map<String, String> param) {
		try {
			String redisData = (String) redisClient.hget("menu_kidsGnb", param.get("menu_stb_svc_id"));
			String kidsMenu = "";
			if(!"".equals(redisData) && redisData != null) {
				List<Object> kidsMenuGnb = CastUtil.StringToJsonList(redisData);	
				List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(kidsMenuGnb);
				
				for(Map<String, Object> kids : data) {
		           // 살아있는 동화 최상위 메뉴 ID 가져오기
		           if(kids.get("kidsz_gnb_cd").equals("70")){
		        	   kidsMenu = (String) kids.get("menu_id"); 
		        	   break;
		           }
				}
			}
			
			return kidsMenu;
		} catch (Exception e) {
			return null;
		}
	}
	// IF-NXPG-403
	public void getContentsLftsynop(Map<String, Object> rtn, Map<String, String> param) {
		try {
			Map<String, Object> synop = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_liveChildStory", param.get("epsd_id")));
			
			contentsService.getContentsCorner(synop, param.get("epsd_id"));
			
			if (synop.containsKey("sris_id") && synop.get("sris_id") != null) {
				Map<String, Object> purchares = contentsService.getContentsPurchares(synop.get("sris_id").toString());
				if (purchares != null && purchares.get("products") != null) {
					List<Map<String, Object>> products = CastUtil.getObjectToMapList(purchares.get("products"));
					DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", true);
					rtn.put("purchares", purchares.get("products"));
				}
				rtn.put("result", "0000");
				rtn.put("contents", synop);
			} else {
				rtn.put("result", "9999");
				rtn.put("contents", null);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
