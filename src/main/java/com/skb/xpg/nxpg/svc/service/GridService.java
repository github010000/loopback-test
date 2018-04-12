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
	            System.out.println("PAGE INFO pageNo : " + pageNo + ", pageCnt : " + pageCnt + ", startNo : " + startNo + ", endNo : " + endNo );
	            gList = new ArrayList<Map<String, Object>>();
	            for (Map<String, Object> grid : gridList.subList(startNo, endNo)) {
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
}
