package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.AddedService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
public class AddedController {

    @Autowired
    AddedService epgService;
    @Autowired
    private Properties properties;
    
    // IF-NXPG-013
    @RequestMapping(value = "/added/epg")
    public Map<String, Object> getAddedEpg(@PathVariable String ver, @RequestParam Map<String, String> param) {
        String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);
		
        rtn.put("IF", IF);
        rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
        
        if (StrUtil.isEmpty(param.get("stb_id"))) {
            rtn.put("result", "9999");
            return rtn;
        }
        
        // 값 불러오기 
        Object resultMap = epgService.getAddedEpg(ver, param);
        // 조회값 없음
        if (resultMap == null) {
            rtn.put("result", "9998");
        }
        // 성공
        else {
            rtn.put("result", "0000");
            rtn.put("channel", resultMap);
        }
        rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
        return rtn;
    }
    
    // IF-NXPG-013
    @RequestMapping(value = "/added/genre")
    public Map<String, Object> getAddedGenre(@PathVariable String ver, @RequestParam Map<String, String> param) {
        String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);
		
        rtn.put("IF", IF);
        rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
        
        if (StrUtil.isEmpty(param.get("stb_id"))) {
            rtn.put("result", "9999");
            return rtn;
        }
        
        // 값 불러오기 
        Object resultMap = epgService.getAddedGenre(ver, param);
        // 조회값 없음
        if (resultMap == null) {
            rtn.put("result", "9998");
        }
        // 성공
        else {
            rtn.put("result", "0000");
            rtn.put("channel", resultMap);
        }
        rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
        return rtn;
    }
}