package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.CwService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class CwController {

	@Autowired
	CwService cwService;
	
	@Autowired
	private Properties properties;


	@RequestMapping(value = "/inter/cwgrid")
	public Map<String, Object> getCwGrid(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);
		
		param.put("UUID", req.getHeader("UUID"));

		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		rtn.put("IF", IF);
		if (StrUtil.isEmpty(param.get("stb_id")) || StrUtil.isEmpty(param.get("cw_call_id"))
				|| StrUtil.isEmpty(param.get("type"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}
		
		// 값 불러오기
		Map<String, Object> resultMap = cwService.cwGetGrid(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("status_code", resultMap.get("status_code"));
			rtn.put("grid", resultMap.get("grid"));
			rtn.put("total_count", resultMap.get("size"));
		}
		
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		LogUtil.tlog(param.get("IF"), "RECV.REQ", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
	
	@RequestMapping(value = "/inter/cwrelation")
	public Map<String, Object> getCwRelation(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);
		
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		rtn.put("IF", IF);
		if (StrUtil.isEmpty(param.get("epsd_rslu_id")) || StrUtil.isEmpty(param.get("epsd_id"))
				|| StrUtil.isEmpty(param.get("type")) || StrUtil.isEmpty(param.get("cw_call_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
			return rtn;
		}
		
		// 값 불러오기
		Map<String, Object> resultMap = cwService.cwGetRelation(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			if("0000".equals(resultMap.get("status_code"))) {
				rtn.put("result", "0000");
				rtn.put("status_code", resultMap.get("status_code"));
				rtn.put("related_info", resultMap.get("relation"));
				rtn.put("total_count", resultMap.get("size"));
				rtn.put("relation_contents", null);
			}else {
				rtn.put("result", "0000");
				rtn.put("status_code", resultMap.get("status_code"));
				rtn.put("relation_contents", resultMap.get("relation"));
				rtn.put("total_count", resultMap.get("size"));
				rtn.put("related_info", null);
			}
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "RECV.REQ", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
	
}
