package com.skb.xpg.nxpg.svc.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class AddedService {

    @Autowired
    private RedisClient redisClient;

    // IF-NXPG-013
    public Object getAddedEpg(String ver,  Map<String, String> param) throws Exception {
    	return CastUtil.StringToJsonList(redisClient.hget(NXPGCommon.EPG_INFO, "epg", param));
    }
    
    // IF-NXPG-017
    public Object getAddedGenre(String ver, Map<String, String> param) throws Exception {
    	return CastUtil.StringToJsonList(redisClient.hget(NXPGCommon.GENRE_INFO, "genre", param));
    }

    // IF-NXPG-018
	public void getRealTimeChannel(Map<String, Object> rtn, Map<String, String> param) throws Exception {
//		Map<String, Object> result = (Map<String, Object>)redisClient.hget(XpgCommon.NXTCHANNELRATING, XpgCommon.NXTCHANNELRATING);
		Map<String, Object> rating = null;
		String json = redisClient.hget(NXPGCommon.CHANNEL_RATING, "channel_rating", param);
		String version = StringUtils.defaultIfEmpty(redisClient.hget(NXPGCommon.VERSION, NXPGCommon.CHANNEL_RATING, param), "0");
		if(json instanceof String){
			rating = CastUtil.StringToJsonMap(json);
		}
		
		if (rating != null) {
			rtn.putAll(rating);
			rtn.put("result", "0000");
			rtn.put("version", version);
//			rtn.put("reason", "");
		} else {
			rtn.put("result", "9998");
//			rtn.put("reason", "실시간 채널순위 데이터가 없습니다.");
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.remove("ver");
	}
}