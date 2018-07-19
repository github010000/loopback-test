package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.GridComparator;
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
public class GridService {

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private MenuService menuService;
	
	// IF-NXPG-006
	public void getGrid(Map<String, Object> rtn, Map<String, String> param) throws Exception {
		Map<String, Object> gridMap = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_CONTENTS, param.get("menu_id"), param));
		
		List<Map<String, Object>> gridList = null;
		List<Map<String, Object>> gList = new ArrayList<>();
		
		rtn.put("version", "");
		
		if (gridMap != null && gridMap.containsKey("contents")) {

			rtn.put("version", gridMap.get("version"));
			
			if (param.containsKey("version") && gridMap.containsKey("version")) {
				String version = gridMap.get("version") + "";
//				if (CastUtil.getStringToLong(param.get("version")) >= CastUtil.getStringToLong(version)) {
//					rtn.put("reason", "최신버전");
//					rtn.put("result", "0000");
//					rtn.put("version", version);
//					return;
//				}

				if (!menuService.doCheckVersion(rtn, param, "version", version, "contents")) {
					rtn.remove("total_count");
					return;
				}
			}
			gridList = CastUtil.getObjectToMapList(gridMap.get("contents"));
			DateUtil.getCompare(gridList, "svc_fr_dt", "svc_to_dt", true);
			
			//셋탑의 화질
			String rslu_type = CastUtil.getString(param.get("rslu_type"));
			String order_type = CastUtil.getString(param.get("order_type"));
			
			int currentCount = 0;
			int tCount = 0;
			if(gridList != null) {
				
				// 그리드 타이틀 정렬 처리
				List<Map<String, Object>> mCopyGrids = null;
				try {
					if(!"".equals(order_type)) {
						mCopyGrids = new ArrayList<Map<String, Object>>(gridList);
						
						Collections.sort(mCopyGrids, new GridComparator(order_type, true));
						gridList = mCopyGrids;
					}
				}
				catch(Exception e){
					LogUtil.error(param.get("IF"), "", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
				}
				
				for (Iterator<Map<String,Object>> iterator = gridList.iterator(); iterator.hasNext() ; ) {
					Map<String, Object> map = CastUtil.getObjectToMap(iterator.next());
					String rslu_typ_cd = CastUtil.getObjectToString(map.get("rslu_typ_cd"));
					
					//진입한 STB의 화질이 콘텐츠의 화질보다 낮을 경우 필터링.(상위 화질은 안보이게 한다.)
					if(rslu_type != null && !rslu_type.isEmpty() && CastUtil.getStringToInteger(rslu_typ_cd)>CastUtil.getStringToInteger(rslu_type)) {
						iterator.remove();
					}
				}
				
				tCount = gridList.size();
				currentCount = tCount;
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
//			gridMap.put("contents", gList);
//			gridMap.put("total_count", currentCount);
			rtn.put("result", "0000");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			rtn.put("contents", gList);
			rtn.put("total_count", currentCount);
		} else {
			rtn.put("result", "9998");
			rtn.put("contents", null);
			rtn.put("total_count", 0);
			if (rtn.get("reason") == null || rtn.get("reason").toString().isEmpty()) {
				rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			}
		}
	}
	
	// IF-NXPG-007
	public Map<String, Object> getGridEvent(String ver, Map<String, String> param) throws Exception {
		Map<String, Object> gridBanner = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_BANNER, param.get("menu_stb_svc_id") + "_" + param.get("menu_id"), param));

		if (gridBanner != null && gridBanner.get("banners") != null) {
			
			String version = CastUtil.getObjectToString( gridBanner.get("version") );
			gridBanner.put("version", version);
//			if (version != null && param.containsKey("version") && !version.isEmpty()
//					&& CastUtil.getStringToLong(param.get("version")) >= CastUtil.getStringToLong(version)) {
//				
//				gridBanner.put("reason", "최신버전");
//				gridBanner.put("result", "0000");
//				gridBanner.put("version", version);
//			} else {
			if (menuService.doCheckVersion(gridBanner, param, "version", version, "banners")) {
		
				List<Map<String, Object>> banners = null;
				if (gridBanner.get("banners") != null) {
					banners = CastUtil.getObjectToMapList(gridBanner.get("banners"));
				}
				menuService.doSegment(banners, param.get("seg_id"), "cmpgn_id");
		        
				DateUtil.getCompare(banners, "dist_fr_dt", "dist_to_dt", true);
			} else {
				gridBanner.remove("total_count");
			}
		
		}
		
		return gridBanner;
	}
	
	public void checkBadge(Map<String, Object> grid) {
		String result = "";
		String badge_typ_nm = "", cacbro_yn = "", sale_prc = "", sris_dist_fir_svc_dt = "", epsd_dist_fir_svc_dt = "";
		String icon_exps_fr_dy = "", icon_exps_to_dy = "", rslu_typ_cd = "", meta_typ_cd = "";
		if (grid.containsKey("badge_typ_nm")) badge_typ_nm = grid.get("badge_typ_nm") + "";
		if (grid.containsKey("cacbro_yn")) cacbro_yn = grid.get("cacbro_yn") + "";
		if (grid.containsKey("sale_prc")) sale_prc = grid.get("sale_prc") + "";
		if (grid.containsKey("meta_typ_cd")) meta_typ_cd = grid.get("meta_typ_cd") + "";
		
		if (grid.containsKey("sris_dist_fir_svc_dt")) sris_dist_fir_svc_dt = grid.get("sris_dist_fir_svc_dt") + "";
		if (grid.containsKey("epsd_dist_fir_svc_dt")) epsd_dist_fir_svc_dt = grid.get("epsd_dist_fir_svc_dt") + "";
		if (grid.containsKey("icon_exps_fr_dt")) icon_exps_fr_dy = grid.get("icon_exps_fr_dt") + "";
		if (grid.containsKey("icon_exps_to_dt")) icon_exps_to_dy = grid.get("icon_exps_to_dt") + "";
		if (grid.containsKey("rslu_typ_cd")) rslu_typ_cd = grid.get("rslu_typ_cd") + "";
		
		if ("02".equals(grid.get("synon_typ_cd"))) {
			
			if ("024".equals(meta_typ_cd) && sris_dist_fir_svc_dt != null && !sris_dist_fir_svc_dt.isEmpty() && DateUtil.getAddDate(sris_dist_fir_svc_dt, 7).compareTo(DateUtil.getYYYYMMDDhhmmss2()) >= 0) {
				result = "sale";
			} else if ("024".equals(meta_typ_cd) && "0".equals(sale_prc)) {
				result = "free";
			} else if ("024".equals(meta_typ_cd)) {
				result = "";
			} else {
				if ("할인".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "sale";
				} else if ("이벤트".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "event";
				} else if (!sris_dist_fir_svc_dt.isEmpty() && DateUtil.getAddDate(sris_dist_fir_svc_dt, 7).compareTo(DateUtil.getYYYYMMDDhhmmss2()) >= 0) {
					result = "new";
				} else if ("0".equals(sale_prc)) {
					result = "free";
				} else if (epsd_dist_fir_svc_dt != null && !epsd_dist_fir_svc_dt.isEmpty() && DateUtil.getAddDate(epsd_dist_fir_svc_dt, 1).compareTo(DateUtil.getYYYYMMDDhhmmss2()) >= 0) {
					result = "up";
				} else if ("Y".equals(cacbro_yn)) {
					result = "rest";
				} else if ("독점".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "monopoly";
				} else if ("35".equals(rslu_typ_cd)) {
					//HDR
					result = "hdr";
				} else if ("30".equals(rslu_typ_cd)) {
					//UHD
					result = "uhd";
				}
			}
		} else if ("01".equals(grid.get("synon_typ_cd"))) {
			if ("024".equals(meta_typ_cd) && !epsd_dist_fir_svc_dt.isEmpty() && DateUtil.getAddDate(epsd_dist_fir_svc_dt, 7).compareTo(DateUtil.getYYYYMMDDhhmmss2()) >= 0) {
				result = "sale";
			} else if ("024".equals(meta_typ_cd) && "0".equals(sale_prc)) {
				result = "free";
			} else if ("024".equals(meta_typ_cd)) {
				result = "";
			} else {
				if ("할인".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "sale";
				} else if ("이벤트".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "event";
				} else if (!epsd_dist_fir_svc_dt.isEmpty() && DateUtil.getAddDate(epsd_dist_fir_svc_dt, 3).compareTo(DateUtil.getYYYYMMDDhhmmss2()) >= 0) {
					result = "new";
				} else if ("0".equals(sale_prc)) {
					result = "free";
				} else if ("독점".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "monopoly";
				} else if ("35".equals(rslu_typ_cd)) {
					//HDR
					result = "hdr";
				} else if ("30".equals(rslu_typ_cd)) {
					//UHD
					result = "uhd";
				}
			}
			
		} else {
			if (!"024".equals(meta_typ_cd)) {
				if ("할인".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "sale";
				} else if ("이벤트".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "event";
				} else if ("독점".equals(badge_typ_nm) && DateUtil.doCompareSingle(icon_exps_fr_dy, icon_exps_to_dy, "yyyyMMddHHmmss")) {
					result = "monopoly";
				}
			}
		}
		
		grid.put("i_img_cd", result);
	}
}
