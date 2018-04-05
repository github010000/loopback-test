package com.skb.xpg.nxpg.svc.service;

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

    // IF-NXPG-102
    public Object getAddedEpg(String ver, Map<String, String> param) {
        try {
            return CastUtil.StringToJsonList((String) redisClient.hget("epg_info", "epg")).get(0);
        } catch (Exception e) {
            return null;
        }
    }
    // IF-NXPG-102
    public Object getAddedGenre(String ver, Map<String, String> param) {
        try {
            return CastUtil.StringToJsonList((String) redisClient.hget("genre_info", "genre")).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}