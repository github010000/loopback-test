package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.ContentsService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
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
	public Map<String, Object> getContentsSynopsis(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("stb_id")) || StrUtil.isEmpty(param.get("sris_id"))
				|| StrUtil.isEmpty(param.get("epsd_id")) || StrUtil.isEmpty(param.get("yn_recent"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> contents = contentsService.getSynopsisContents(ver, param);
		Map<String, Object> purchares = contentsService.getContentsPurchares(ver, param);
		// 조회값 없음
		if (contents == null) {
			rtn.put("result", "9998");
		} else {
			// 성공
			contentsService.getContentsPeople(contents, param);
			contentsService.getContentsCorner(contents, param);
			contentsService.getContentsPreview(contents, param);
			contentsService.getContentsReview(contents, param);
			contents.put("series_info", contentsService.getContentsSeries(ver, param));
			rtn.put("result", "0000");
			rtn.put("contents", contents);
			if (purchares != null && purchares.get("products") != null) {
				rtn.put("purchares", purchares.get("products"));
			}
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-011
	@RequestMapping(value = "/contents/people")
	public Map<String, Object> getContentsPeople(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("prs_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = contentsService.getContentsPeople(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-014
	@RequestMapping(value = "/contents/gwsynop")
	public Map<String, Object> getContentsGwsynop(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		List<Map<String, Object>> resultMap = contentsService.getContentsGwsynop(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-015
	@RequestMapping(value = "/contents/commerce")
	public Map<String, Object> getContentsCommerce(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("sris_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = contentsService.getContentsCommerce(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
	// IF-NXPG-016
	@RequestMapping(value = "/contents/corner")
	public Map<String, Object> getContentsConer(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("cnr_grp_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = contentsService.getCornerGather(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.putAll(resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
	// IF-NXPG-017
	@RequestMapping(value = "/contents/vodlist")
	public Map<String, Object> getContentsVodList(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = contentsService.getContentsVodList(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
	// IF-NXPG-008 contents_review
	@RequestMapping(value = "/contents/rating")
	public Map<String, Object> getContentsReview(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("sris_id"))
				|| StrUtil.isEmpty(param.get("page_no")) || StrUtil.isEmpty(param.get("page_cnt"))
				|| StrUtil.isEmpty(param.get("site_cd"))
				) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = contentsService.getContentsReview(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-011 people
	@RequestMapping(value = "/people/info")
	public Map<String, Object> getPeopleInfo(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("prs_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> resultMap = contentsService.getPeopleInfo(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
}
