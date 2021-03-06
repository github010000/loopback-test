package com.jung.sys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jung.sys.common.ResultCommon;
import com.jung.sys.config.Properties;
import com.jung.sys.service.AddedService;
import com.jung.sys.util.DateUtil;
import com.jung.sys.util.LogUtil;

@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class AddedController {

    @Autowired
    AddedService epgService;
    @Autowired
    private Properties properties;
    
    // IF-NXPG-013
    @RequestMapping(value = "/added/epg")
    public Map<String, Object> getAddedEpg(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
        String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);
		
        rtn.put("IF", IF);
        rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_start", System.nanoTime() + "");
		param.put("redis_count", "0");
        
//        if (StrUtil.isEmpty(param.get("stb_id"))) {
//            rtn.put("result", "9999");
//            return rtn;
//        }
        
        // 값 불러오기 
        Object resultMap = null;
        try {
        	resultMap = epgService.getAddedEpg(ver, param);
        } catch (Exception e) {
        	LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
        }
        // 조회값 없음
        if (resultMap == null) {
            rtn.put("result", "9998");
        }
        // 성공
        else {
            rtn.put("result", "0000");
            rtn.put("channel", resultMap);
        }
        rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
        
        rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
        
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
		
        return rtn;
    }
    
    // IF-NXPG-017
    @RequestMapping(value = "/added/epggenre")
    public Map<String, Object> getAddedGenre(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
        String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);
		
        rtn.put("IF", IF);
        rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
        
//        if (StrUtil.isEmpty(param.get("stb_id"))) {
//            rtn.put("result", "9999");
//            return rtn;
//        }
        
        // 값 불러오기 
        Object resultMap = null;
        try {
        	resultMap = epgService.getAddedGenre(ver, param);
        } catch (Exception e) {
        	LogUtil.error(param.get("IF"), param.get("UUID"), param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
        }
        // 조회값 없음
        if (resultMap == null) {
            rtn.put("result", "9998");
        }
        // 성공
        else {
            rtn.put("result", "0000");
            rtn.put("channel", resultMap);
        }
        
        rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
        rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
        return rtn;
    }
    
    // IF-NXPG-018
    @RequestMapping(value = "/added/realtimechannel")
    public Map<String, Object> getRealTimeChannel(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
        String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);
		
        rtn.put("IF", IF);
        rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
        
//        if (StrUtil.isEmpty(param.get("stb_id"))) {
//            rtn.put("result", "9999");
//            return rtn;
//        }
        
        // 값 불러오기 
        try {
        	epgService.getRealTimeChannel(rtn, param);
        } catch (Exception e) {
        	LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
        	rtn.put("result", "9997");
        }

        rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
        rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
        return rtn;
    }
}