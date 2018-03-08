package com.skb.xpg.nxpg.svc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.svc.XpgService;

@RestController
@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
public class XpgController {

	@Autowired
	private XpgService xpgService;
	
//	@RequestMapping(value = "/contents/synopsis")
//	public Map<String, Object> getSample(@PathVariable String ver, @RequestParam Map<String, String> param
//			, @PathVariable String sub1, @PathVariable String sub2) {
//		
//		return xpgService.hgetRedis(ver, sub1, sub2, param);
//	}
	
	
}
