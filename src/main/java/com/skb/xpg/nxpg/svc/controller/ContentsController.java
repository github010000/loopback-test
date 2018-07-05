package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.ContentsService;
import com.skb.xpg.nxpg.svc.util.CiModeUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class ContentsController {

	@Autowired
	ContentsService contentsService;
	
	@Autowired
	private Properties properties;

	// IF-NXPG-010
	@RequestMapping(value = "/contents/synopsis")
	public Map<String, Object> getContentsSynopsis(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id"))
				|| StrUtil.isEmpty(param.get("search_type"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}
		
		if (("1").equals(param.get("search_type")) && StrUtil.isEmpty(param.get("epsd_id"))
			|| ("2").equals(param.get("search_type")) && StrUtil.isEmpty(param.get("epsd_rslu_id"))
			|| ("3").equals(param.get("search_type")) && StrUtil.isEmpty(param.get("sris_id")) && StrUtil.isEmpty(param.get("epsd_id")))
			{
			rtn.put("result", "9999");
			rtn.put("reason", "search_type에 맞는 param입력 요망");
			return rtn;
		}

		// 값 불러오기
		try {
			contentsService.getSynopsisContents(rtn, param);
			
			
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
			rtn.put("result", "9997");
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}

	// IF-NXPG-011
	@RequestMapping(value = "/contents/people")
	public Map<String, Object> getContentsPeople(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("prs_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getContentsPeople(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());	
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}

	// IF-NXPG-014
	@RequestMapping(value = "/contents/gwsynop")
	public Map<String, Object> getContentsGwsynop(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (("1").equals(param.get("search_type")) && StrUtil.isEmpty(param.get("sris_id"))
			|| ("2").equals(param.get("search_type")) && StrUtil.isEmpty(param.get("prd_prc_id")))
			{
			rtn.put("result", "9999");
			rtn.put("reason", "search_type에 맞는 param입력 요망");
			rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
			LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
			return rtn;
		}
		
		if ("2".equals(param.get("search_type"))) {
			param.put("sris_id", param.get("prd_prc_id"));
		}
		
		if (StrUtil.isEmpty(param.get("sris_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
			LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getContentsGwsynop(ver, param);
			
			if(NXPGCommon.isCIMode()) {
				CiModeUtil.getFilter(resultMap);
			}
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("package", resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}

	// IF-NXPG-015
	@RequestMapping(value = "/contents/commerce")
	public Map<String, Object> getContentsCommerce(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("sris_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getContentsCommerce(ver, param);
			if(NXPGCommon.isCIMode()) {
				CiModeUtil.getFilter(resultMap);
			}

		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("commerce", resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
	
	// IF-NXPG-016
	@RequestMapping(value = "/contents/corner")
	public Map<String, Object> getContentsConer(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("cnr_grp_id"))) {
			rtn.put("result", "9999");
			rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getCornerGather(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.putAll(resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
	
	// IF-NXPG-017
	@RequestMapping(value = "/contents/vodlist")
	public Map<String, Object> getContentsVodList(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getContentsVodList(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
	
	// IF-NXPG-008 contents_review
	@RequestMapping(value = "/contents/rating")
	public Map<String, Object> getContentsReview(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("sris_id"))
				|| StrUtil.isEmpty(param.get("page_no")) || StrUtil.isEmpty(param.get("page_cnt"))
				|| StrUtil.isEmpty(param.get("site_cd"))
				) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getContentsReview(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}

	// IF-NXPG-011 people
	@RequestMapping(value = "/people/info")
	public Map<String, Object> getPeopleInfo(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("prs_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = null;
		try {
			resultMap = contentsService.getPeopleInfo(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
}
