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
public class KidsService {

	@Autowired
	private RedisClient redisClient;
	
	// IF-NXPG-101
	public List getMenuKzchar(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsCharacter", param.get("menu_stb_svc_id")));
		} catch (Exception e) {
			return null;
		}
	}
	// IF-NXPG-102
	public List getMenuKzgnb(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsGnb", param.get("menu_stb_svc_id")));
		} catch (Exception e) {
			return null;
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
	public Map<String, Object> getContentsLftsynop(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_liveChildStory", param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
