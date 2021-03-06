package com.jung.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jung.sys.common.NXPGCommon;
import com.jung.sys.common.ResultCommon;
import com.jung.sys.redis.RedisClient;
import com.jung.sys.util.CastUtil;
import com.jung.sys.util.DateUtil;
import com.jung.sys.util.GridComparator;
import com.jung.sys.util.LogUtil;

@Service
public class KidsService {

	@Autowired
	private RedisClient redisClient;
	@Autowired
	ContentsService contentsService;
	
	// IF-NXPG-101
	public void getMenuKzchar(Map<String, Object> rtn, Map<String, String> param) throws Exception {
		String version = StringUtils.defaultIfEmpty(redisClient.hget(NXPGCommon.VERSION, NXPGCommon.MENU_KIDSCHARACTER + "_" + param.get("menu_stb_svc_id"), param), "");
		String order_type=CastUtil.getString(param.get("order_type"));
		
		rtn.put("version", version);
		
		if (version != null && param.containsKey("version") && !version.isEmpty()
				&& CastUtil.getStringToLong(param.get("version")) >= CastUtil.getStringToLong(version)) {
			rtn.put("reason", "최신버전");
			rtn.put("result", "0000");
		} else {
			
			List<Map<String, Object>> kzgnb = CastUtil.StringToJsonListMap(redisClient.hget(NXPGCommon.MENU_KIDSCHARACTER, param.get("menu_stb_svc_id"), param));
					
			
			if (kzgnb == null) {
				rtn.put("result", "9998");
			} else {
				DateUtil.getCompare(kzgnb, "dist_fr_dt", "dist_to_dt", true);
				
				// 그리드 타이틀 정렬 처리
				List<Map<String, Object>> mCopyGrids = null;
				try {
					if(!"".equals(order_type)) {
						mCopyGrids = new ArrayList<Map<String, Object>>(kzgnb);
						
						Collections.sort(mCopyGrids, new GridComparator(order_type, true));
						kzgnb = mCopyGrids;
					}
				}
				catch(Exception e){
					LogUtil.error(param.get("IF"), "", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
				}
				
				rtn.put("result", "0000");
				rtn.put("menus", kzgnb);
				// 카운트 넣어주기
				if (kzgnb != null) {
					rtn.put("total_count", kzgnb.size());
				}
			}
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		}
	}
	
	// IF-NXPG-102
	public void getMenuKzgnb(Map<String, Object> rtn, Map<String, String> param) throws Exception {
		String version = StringUtils.defaultIfEmpty(redisClient.hget(NXPGCommon.VERSION, NXPGCommon.MENU_KIDSGNB + "_" + param.get("menu_stb_svc_id"), param), "");
		rtn.put("version", version);
		
		if (version != null && param.containsKey("version") && !version.isEmpty()
				&& CastUtil.getStringToLong(param.get("version")) >= CastUtil.getStringToLong(version)) {
			rtn.put("reason", "최신버전");
			rtn.put("result", "0000");
		} else {
			
			List<Object> kzgnb = CastUtil.StringToJsonList(redisClient.hget(NXPGCommon.MENU_KIDSGNB, param.get("menu_stb_svc_id"), param));
			
			if (kzgnb == null) {
				rtn.put("result", "9998");
			} else {
				DateUtil.getCompareObject(kzgnb, "dist_fr_dt", "dist_to_dt", true);
				
				rtn.put("result", "0000");
				rtn.put("menus", kzgnb);
				// 카운트 넣어주기
				if (kzgnb != null) {
					rtn.put("total_count", kzgnb.size());
				}
			}
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		}
	}
	
	// IF-NXPG-401
	public String getMenulfthomemapping(String ver, Map<String, String> param) throws Exception {
		String redisData = redisClient.hget(NXPGCommon.MENU_KIDSGNB, param.get("menu_stb_svc_id"), param);
		String kidsMenu = "";
		// 살아있는 동화
		
		if(redisData != null && !"".equals(redisData)) {
			List<Object> kidsMenuGnb = CastUtil.StringToJsonList(redisData);	
			List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(kidsMenuGnb);
			
			for(Map<String, Object> kids : data) {
	           // 살아있는 동화 최상위 메뉴 ID 가져오기
	           if(("70").equals(kids.get("kidsz_gnb_cd"))){
	        	   kidsMenu = CastUtil.getObjectToString(kids.get("menu_id"));
//		        	   kidsMenu = (String) kids.get("menu_id"); 
	        	   break;
	           }
			}
		}
		
		return kidsMenu;
	}
	
	// IF-NXPG-403
	public void getContentsLftsynop(Map<String, Object> rtn, Map<String, String> param) throws Exception {
		Map<String, Object> synop = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_LIVECHILDSTORY, param.get("epsd_id"), param));
		
		if (synop == null) {
			rtn.put("result", "9998");
			rtn.put("contents", null);
		} else {
			contentsService.getContentsCorner(synop, param.get("epsd_id"), param);
			
			if (synop.containsKey("sris_id") && synop.get("sris_id") != null) {
				Map<String, Object> purchares = contentsService.getContentsPurchares(synop.get("sris_id").toString(), param);
				if (purchares != null && purchares.get("products") != null) {
					List<Map<String, Object>> products = CastUtil.getObjectToMapList(purchares.get("products"));
					DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", false);
					rtn.put("purchares", purchares.get("products"));
				}
				synop.put("menu_id", CastUtil.getString(param.get("cur_menu")));
				rtn.put("result", "0000");
				rtn.put("contents", synop);
			} else {
				rtn.put("result", "9999");
				rtn.put("contents", null);
			}
		}
	}
}
