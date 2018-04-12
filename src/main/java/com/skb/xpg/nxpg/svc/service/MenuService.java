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
public class MenuService {

	@Autowired
	private RedisClient redisClient;
	
	// IF-NXPG-001
	public void getMenuGnb(String ver, Map<String, String> param, Map<String, Object> rtn) {
		try {
			String redisData = (String) redisClient.hget(NXPGCommon.MENU_GNB, param.get("menu_stb_svc_id"));
			if(!"".equals(redisData) && redisData != null) {
				List<Object> menugnb = CastUtil.StringToJsonList(redisData);			
				List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(menugnb);
				List<Map<String, Object>> gnb = DateUtil.getCompare(data);
				
				String version = StringUtils.defaultIfEmpty((String) redisClient.hget("version",NXPGCommon.MENU_GNB), "");
				
				if (gnb == null) {
					rtn.put("result", "9998");
				}
				// 성공
				else {
					rtn.put("version", version);
					rtn.put("result", "0000");
					rtn.put("menus", gnb);
					// 카운트 넣어주기 
					if (gnb != null) rtn.put("total_count", gnb.size());
				}
			}else {
				rtn.put("result", "9998");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// IF-NXPG-002
	public void getMenuAll(String ver, Map<String, String> param, Map<String, Object> rtn) {
		try {
			String redisData = (String) redisClient.hget(NXPGCommon.MENU_ALL, param.get("menu_stb_svc_id"));
			if(!"".equals(redisData) && redisData != null) {
				List<Object> menuall = CastUtil.StringToJsonList(redisData);
				List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(menuall);
				List<Map<String, Object>> all = DateUtil.getCompare(data);
				
				String version = StringUtils.defaultIfEmpty((String) redisClient.hget("version",NXPGCommon.MENU_ALL), "");
				
				// 조회값 없음
				if (all == null) {
					rtn.put("result", "9998");
				}
				// 성공
				else {
					rtn.put("version", version);
					rtn.put("result", "0000");
					rtn.put("menus", all);
					// 카운트 넣어주기 
					if (all != null) rtn.put("total_count", all.size());
				}
			}else {
				rtn.put("result", "9998");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getBlockBigBanner(String ver, Map<String, String> param) {
		try {
//			System.out.println("11111 ::: " + param.get("menu_stb_svc_id") +  "_" + param.get("menu_id"));
			Map<String, Object> bigbanner = CastUtil.StringToJsonMap((String) redisClient.hget("big_banner", param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));

			bigbanner.put("banner_count", bigbanner.get("total_count"));
			bigbanner.remove("total_count");
			
			return bigbanner;
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-004
	public Map<String, Object> getBlockBlock(String ver, Map<String, String> param) {
		try {
			
			Map<String, Object> blockblock = CastUtil.StringToJsonMap((String) redisClient.hget("block_block", param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
			List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
			for (Object block : blocks) {
				Map<String, Object> map = CastUtil.getObjectToMap(block);

				Map<String, Object> gridbanner = getGridBanner(param.get("menu_stb_svc_id") + "_" + map.get("menu_id").toString());
				map.put("menus", null);
				if (gridbanner != null) {
					map.put("menus", gridbanner.get("banners"));
				}
			}
			blockblock.put("block_count", blockblock.get("total_count"));
			blockblock.remove("total_count");
			return blockblock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getGridBanner(String menu_id) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("grid_banner", menu_id));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// IF-NXPG-005
	public Map<String, Object> getBlockMonth(String ver, Map<String, String> param) {
		try {
			Map<String, Object> blockblock = CastUtil.StringToJsonMap((String) redisClient.hget("block_block", param.get("menu_id")));
			List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
			for (Object block : blocks) {
				Map<String, Object> map = CastUtil.getObjectToMap(block);
				
				List gridbanner = getGridMonth(map.get("menu_id").toString());
				System.out.println(gridbanner.toString());
				
				map.put("menus", gridbanner);
			}
			blockblock.put("block_count", blockblock.get("total_count"));
			blockblock.remove("total_count");
			return blockblock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// IF-NXPG-005
	public List getGridMonth(String menu_id) {
		try {
			System.out.println(redisClient.hget("block_month", menu_id));
			return CastUtil.StringToJsonList((String) redisClient.hget("block_month", menu_id));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
