package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;

@Service
public class GridService {

	@Autowired
	private RedisClient redisClient;
	
	// IF-NXPG-006
	public Map<String, Object> getGrid(String ver, Map<String, String> param) {
		try {
			Map<String, Object> gridMap = CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.GRID_CONTENTS, param.get("menu_id")));
			
			List<Map<String, Object>> gridList = CastUtil.getObjectToMapList(gridMap.get("contents"));
			List<Map<String, Object>> gList = new ArrayList<>();
			
			DateUtil.getCompare(gridList, "svc_fr_dt", "svc_to_dt", false);
			
			String gridStr = "";
			int tCount = 0;
			if(gridList != null) {
				tCount = gridList.size();
				int pageNo = CastUtil.getStringToInteger(param.get("page_no"));
	            int pageCnt = CastUtil.getStringToInteger(param.get("page_cnt"));
	            
	            int startNo = ((pageNo - 1 < 0) ? 0 : ((pageNo - 1) * pageCnt));
	            int endNo = ((pageNo < 0) ? tCount : (startNo + pageCnt));
	      
	            endNo = (endNo > tCount) ? tCount : endNo;
//	            System.out.println("PAGE INFO pageNo : " + pageNo + ", pageCnt : " + pageCnt + ", startNo : " + startNo + ", endNo : " + endNo );
	            gList = new ArrayList<Map<String, Object>>();
	            for (Map<String, Object> grid : gridList.subList(startNo, endNo)) {
	            	checkBadge(grid);
	            	gList.add(grid);
	            }
	            tCount = gList.size();
			}
			gridMap.put("contents", gList);
			gridMap.put("total_count", tCount);
			
			return gridMap;
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-007
	public Map<String, Object> getGridEvent(String ver, Map<String, String> param) {
		try {
			Map<String, Object> gridBanner = CastUtil.StringToJsonMap((String) redisClient.hget("grid_banner", param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));

			List<Map<String, Object>> banners = null;
			if (gridBanner.get("banners") != null) {
				banners = CastUtil.getObjectToMapList(gridBanner.get("banners"));
			}
			DateUtil.getCompare(banners, "dist_fr_dt", "dist_to_dt", false);
			
			return gridBanner;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void checkBadge(Map<String, Object> grid) {
		String result = "";
		String badge_typ_nm = "", cacbro_yn = "", sale_prc = "", sris_dist_fir_svc_dt = "", epsd_dist_fir_svc_dt = "";
		String icon_exps_fr_dy = "", icon_exps_to_dy = "", rslu_typ_cd = "", meta_typ_cd = "";
		if (grid.containsKey("badge_typ_nm")) badge_typ_nm = grid.get("badge_typ_nm") + "";
		if (grid.containsKey("cacbro_yn")) cacbro_yn = grid.get("cacbro_yn") + "";
		if (grid.containsKey("sale_prc")) sale_prc = grid.get("sale_prc") + "";
		if (grid.containsKey("meta_typ_cd")) meta_typ_cd = grid.get("meta_typ_cd") + "";
		
		if (grid.containsKey("sris_dist_fir_svc_dt")) sris_dist_fir_svc_dt = grid.get("sris_dist_fir_svc_dt") + "";
		if (grid.containsKey("epsd_dist_fir_svc_dt")) epsd_dist_fir_svc_dt = grid.get("epsd_dist_fir_svc_dt") + "";
		if (grid.containsKey("icon_exps_fr_dy")) icon_exps_fr_dy = grid.get("icon_exps_fr_dy") + "";
		if (grid.containsKey("icon_exps_to_dy")) icon_exps_to_dy = grid.get("icon_exps_to_dy") + "";
		if (grid.containsKey("rslu_typ_cd")) rslu_typ_cd = grid.get("rslu_typ_cd") + "";
		
		if ("01".equals(grid.get("synon_typ_cd"))) {
			
			if ("26".equals(meta_typ_cd) && DateUtil.getAddDate(sris_dist_fir_svc_dt, 3).compareTo(DateUtil.getYYYYMMDDhhmmss()) <= 0) {
				result = "sale";
			} else if ("26".equals(meta_typ_cd) && "0".equals(sale_prc)) {
				result = "free";
			} else if ("할인".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "sale";
			} else if ("이벤트".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "event";
			} else if (DateUtil.getAddDate(sris_dist_fir_svc_dt, 7).compareTo(DateUtil.getYYYYMMDDhhmmss()) <= 0) {
				result = "new";
			} else if ("0".equals(sale_prc)) {
				result = "free";
			} else if (DateUtil.getAddDate(epsd_dist_fir_svc_dt, 1).compareTo(DateUtil.getYYYYMMDDhhmmss()) <= 0) {
				result = "";
			} else if ("Y".equals(cacbro_yn)) {
				result = "";
			} else if ("독점".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "monopoly";
			} else if ("35".equals(rslu_typ_cd)) {
				//HDR
				result = "hdr";
			} else if ("30".equals(rslu_typ_cd)) {
				//UHD
				result = "uhd";
			}
		} else if ("02".equals(grid.get("synon_typ_cd"))) {
			if ("26".equals(meta_typ_cd) && DateUtil.getAddDate(epsd_dist_fir_svc_dt, 3).compareTo(DateUtil.getYYYYMMDDhhmmss()) <= 0) {
				result = "sale";
			} else if ("26".equals(meta_typ_cd) && "0".equals(sale_prc)) {
				result = "free";
			} else if ("할인".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "sale";
			} else if ("이벤트".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "event";
			} else if (DateUtil.getAddDate(epsd_dist_fir_svc_dt, 3).compareTo(DateUtil.getYYYYMMDDhhmmss()) <= 0) {
				result = "new";
			} else if ("0".equals(sale_prc)) {
				result = "free";
			} else if ("독점".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "monopoly";
			} else if ("35".equals(rslu_typ_cd)) {
				//HDR
				result = "hdr";
			} else if ("30".equals(rslu_typ_cd)) {
				//UHD
				result = "uhd";
			}
		} else {
			if ("할인".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "sale";
			} else if ("이벤트".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "event";
			} else if ("독점".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMdd")) {
				result = "monopoly";
			}
		}
		
		grid.put("i_img_cd", result);
	}
}
