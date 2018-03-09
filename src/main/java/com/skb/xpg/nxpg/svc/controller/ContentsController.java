//package com.skb.xpg.nxpg.svc.controller;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.skb.xpg.nxpg.svc.svc.ContentsService;
//
//@RestController
//@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
//public class ContentsController {
//	
//	@Autowired
//	ContentsService contentsService;
//	
//	@RequestMapping(value = "/contents/synopsis")
//	public Map<String, Object> getSynopsis(@PathVariable String ver, @RequestParam Map<String, String> param) {
//		
//		return contentsService.getSynopsis(ver, param);
//	}
//	
//	@RequestMapping(value = "/contents/synopsis2")
//	public Map<String, Object> getSynopsis2(@PathVariable String ver, @RequestParam Map<String, String> param) {
//		
//		return contentsService.getSynopsis2(ver, param);
//	}
//}
