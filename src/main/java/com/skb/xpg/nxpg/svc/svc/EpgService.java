package com.skb.xpg.nxpg.svc.svc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class EpgService {

    @Autowired
    private RedisClient redisClient;

    // IF-NXPG-102
    public List<Object> getAddedEpg(String ver, Map<String, String> param) {
        try {
            return CastUtil.StringToJsonList((String) redisClient.hget("epg_info", "epg"));
        } catch (Exception e) {
            return null;
        }
    }
}