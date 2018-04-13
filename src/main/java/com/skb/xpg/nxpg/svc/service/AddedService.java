package com.skb.xpg.nxpg.svc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class AddedService {

    @Autowired
    private RedisClient redisClient;

    // IF-NXPG-013
    public Object getAddedEpg(String ver, Map<String, String> param) {
        try {
            return CastUtil.StringToJsonList((String) redisClient.hget("epg_info", "epg")).get(0);
        } catch (Exception e) {
            return null;
        }
    }
    // IF-NXPG-017
    public Object getAddedGenre(String ver, Map<String, String> param) {
        try {
            return CastUtil.StringToJsonList((String) redisClient.hget("genre_info", "genre")).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // IF-NXPG-018
	public void getRealTimeChannel(Map<String, Object> rtn) {
//		Map<String, Object> result = (Map<String, Object>)redisClient.hget(XpgCommon.NXTCHANNELRATING, XpgCommon.NXTCHANNELRATING);
		List<Object> rating = null;
		String json = (String) redisClient.hget("channel_rating", "channel_rating");
		if(json instanceof String){
			rating = CastUtil.StringToJsonList(json);
		}
		if (rating != null) {
			rtn.put("result", "0000");
			rtn.put("reason", "");
			rtn.put("rating", rating);
		} else {
			rtn.put("result", "9999");
			rtn.put("reason", "실시간 채널순위 데이터가 없습니다.");
		}
	}
}