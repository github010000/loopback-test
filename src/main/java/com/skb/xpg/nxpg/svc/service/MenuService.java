package com.skb.xpg.nxpg.svc.service;

import java.util.List;
import java.util.Map;

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
	public List getMenuGnb(String ver, Map<String, String> param) {
		try {
			List<Object> menugnb = CastUtil.StringToJsonList((String) redisClient.hget(NXPGCommon.MENU_GNB, param.get("menu_stb_svc_id")));
			List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(menugnb);
			List<Map<String, Object>> gnb = DateUtil.getCompare(data);
			
			return gnb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// IF-NXPG-002
	public List getMenuAll(String ver, Map<String, String> param) {
		try {
			List<Object> menuall = CastUtil.StringToJsonList((String) redisClient.hget(NXPGCommon.MENU_ALL, param.get("menu_stb_svc_id")));
			List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(menuall);
			List<Map<String, Object>> all = DateUtil.getCompare(data);
			
			return all;
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getBlockBigBanner(String ver, Map<String, String> param) {
		try {
			Map<String, Object> bigbanner = CastUtil.StringToJsonMap((String) redisClient.hget("big_banner", param.get("menu_id")));

			bigbanner.put("banner_count", bigbanner.get("total_count"));
			bigbanner.remove("total_count");
			
			return bigbanner;
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getBlockBlock(String ver, Map<String, String> param) {
		try {
			Map<String, Object> blockblock = CastUtil.StringToJsonMap((String) redisClient.hget("block_block", param.get("menu_id")));
			List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
			for (Object block : blocks) {
				Map<String, Object> map = CastUtil.getObjectToMap(block);

				Map<String, Object> gridbanner = getGridBanner(map.get("menu_id").toString());
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
