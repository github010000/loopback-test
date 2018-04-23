package com.skb.xpg.nxpg.svc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
public class AddedService {

    @Autowired
    private RedisClient redisClient;

    // IF-NXPG-013
    public Object getAddedEpg(String ver,  Map<String, String> param) {
    	try {
    		return CastUtil.StringToJsonList(redisClient.hget(NXPGCommon.EPG_INFO, "epg"));
    	} catch (Exception e) {
    		LogUtil.error(e.getStackTrace(), param.get("IF"), "", "", param.get("stb_id"), "", "");
            return null;
    	}
    }
    // IF-NXPG-017
    public Object getAddedGenre(String ver, Map<String, String> param) {
        try {
            return CastUtil.StringToJsonList(redisClient.hget(NXPGCommon.GENRE_INFO, "genre"));
        } catch (Exception e) {
        	LogUtil.error(e.getStackTrace(), param.get("IF"), "", "", param.get("stb_id"), "", "");
            return null;
        }
    }

    // IF-NXPG-018
	public void getRealTimeChannel(Map<String, Object> rtn) {
//		Map<String, Object> result = (Map<String, Object>)redisClient.hget(XpgCommon.NXTCHANNELRATING, XpgCommon.NXTCHANNELRATING);
		Map<String, Object> rating = null;
		String json = redisClient.hget(NXPGCommon.CHANNEL_RATING, "channel_rating");
		if(json instanceof String){
			rating = CastUtil.StringToJsonMap(json);
		}
		if (rating != null) {
			rtn.putAll(rating);
			rtn.put("result", "0000");
			rtn.put("reason", "");
		} else {
			rtn.put("result", "9999");
			rtn.put("reason", "실시간 채널순위 데이터가 없습니다.");
		}
	}
}