package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.ArrayAnnotationValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.rest.RestClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class CwService {
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private Properties properties;
	
	@Autowired
	private RestClient restClient;
	
	@Autowired
	@Qualifier("cwBaseUrl")
	private String cwBaseUrl;
	
	@Autowired
	@Qualifier("cwUser")
	private String cwUser;
	
	@Autowired
	@Qualifier("cwPassword")
	private String cwPassword;
	
	
	public List<Map<String, Object>> cwGetGrid(String ver, Map<String, String> param) {
		
		String type = (String)param.get("type");
		List<Map<String, Object>> resultList = null;
		Map<String, Object> temp = null;
		
		switch (type) {
		
		case "section":
			param.put("retrieveSections", "NONE");
			temp = cwCall("onlysection", param);
			break;
		case "multi":
			temp = cwCall("multisection", param);
			break;
		case "all":
			temp = cwCall("fullsection", param);
			break;
		case "onepage":
			temp = cwCall("getonepage", param);
			break;
		case "onesection":
			temp = cwCall("getonesection", param);
			break;
		}
		
		resultList = makeCwGrid(temp);
		
		return resultList;
		
	}
	
	//API 호출 메소드
	public Map<String, Object> cwCall(String type, Map<String, String> param){

		Map<String, Object> objMap;
		Map<String, Object> cw = properties.getCw();
		Map<String, Object> keyAndValue;
		
		String cwparam = "";

	
		keyAndValue = CastUtil.getObjectToMap(cw.get(type));
					
		String path = keyAndValue.get("uri").toString();
		String requestparam = keyAndValue.get("param").toString();
		String nxpgparam = keyAndValue.get("nxpgparam").toString();
		
		//경로에 cw call id 치환
		String regex = "\\{(.*?)\\}";
		path = path.replaceAll(regex, param.get("cw_call_id"));
		
		String[] arrRequestParam = requestparam.split(",");
		String[] arrNxpgParam = nxpgparam.split(",");
		
		if (arrRequestParam != null) {
			for (int a = 0; a < arrNxpgParam.length; a++) {
				if (!arrRequestParam[a].isEmpty()) {
					cwparam += ";" + arrRequestParam[a] + "|" + param.get(arrNxpgParam[a]);
				}
			}
		}
		if (!cwparam.isEmpty() && cwparam.length() > 1) {
			cwparam = cwparam.substring(1);
		}
		
		System.out.println(cwparam);
		String rest = null;
		rest = restClient.getRestUri(cwBaseUrl + path, cwUser, cwPassword, cwparam);
		objMap = CastUtil.StringToJsonMap(rest);

		
		return objMap;
	}
	
	
	public List<Map<String, Object>> makeCwGrid(Map<String, Object> objMap) {
		
		//데이터 추출로직 시작
		List<Map<String, Object>> sections = (List<Map<String, Object>>) objMap.get("sections");
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		
		List<String> idList = new ArrayList<String>();
		
		if(sections != null) {
			for(Map<String, Object>sectionMap:sections) {
				List<Map<String, Object>> blocks = null;
				blocks = (List<Map<String, Object>>) sectionMap.get("blocks");
				if(blocks != null) {
					for(Map<String, Object>blockMap:blocks) {
						List<Map<String, Object>> items = null;
						items = (List<Map<String, Object>>) blockMap.get("items");
						if(items != null) {
							for(Map<String, Object>itemMap:items) {
								Map<String, Object> itemIds = (Map<String, Object>) itemMap.get("itemIds");
								String contentId = (String) itemIds.get("CW");
								idList.add(contentId);
							}
						}
						result.put("blockTitle",blockMap.get("blockLabel"));
						result.put("idList",idList);
						idList = new ArrayList<String>();
					}
				}
				//응답값에 sectionId가 없으면 multisection이 호출된 것이다.
				if(sectionMap.get("sectionId")!=null) {
					result.put("sectionId", sectionMap.get("sectionId"));
					resultList.add(result);
					result = new HashMap<String, Object>();
				}else {	
//					if() {
						//TODO 단일섹션 처리
//					}else {
						
						//멀티섹션 처리 로직
						List<Map<String, Object>> items = null;
						items = (List<Map<String, Object>>) sectionMap.get("items");
						if(items != null) {
							for(Map<String, Object>itemMap:items) {
								Map<String, Object> itemIds = (Map<String, Object>) itemMap.get("itemIds");
								String contentId = (String) itemIds.get("CW");
								idList.add(contentId);
							}
						}
						result.put("blockTitle",sectionMap.get("blockLabel"));
						result.put("sectionId", sectionMap.get("blockId"));
						result.put("idList",idList);
						idList = new ArrayList<String>();
						resultList.add(result);
						result = new HashMap<String, Object>();
//					}
				}
			}
		}
		System.out.println("::::::"+resultList);
		//데이터 추출로직 끝
		
		//데이터 가공로직 시작
		List<Map<String, Object>>finalCwGrid = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object>temp:resultList ) {
			List<Map<String, Object>>cwGrid = new ArrayList<Map<String,Object>>();
			Map<String, Object> cwMap = new HashMap<String, Object>();
			List<String>tempIdList = (List<String>) temp.get("idList");
			if(tempIdList != null) {
				for(String epsd_rslu_id:tempIdList) {
					Map<String, Object> gridData = new HashMap<String, Object>();
					Map<String, Object> cidInfo = CastUtil.StringToJsonMap((String) redisClient.hget("contents_cidinfo",epsd_rslu_id));
					try {
						String sris_id = (String) cidInfo.get("sris_id");
						
						Map<String, Object> contentData = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_contents",sris_id));
						
						gridData.put("poster_filename_h", contentData.get("sris_poster_filename_h"));
						gridData.put("sris_id", contentData.get("sris_id"));
						gridData.put("poster_filename_v", contentData.get("sris_poster_filename_v"));
//						gridData.put("sris_nm", contentData.get("sris_id"));
						gridData.put("epsd_id", contentData.get("epsd_id"));
						gridData.put("adlt_lvl_cd", contentData.get("adlt_lvl_cd"));
						gridData.put("title", contentData.get("title"));						
						
						cwGrid.add(gridData);
						
					}catch (Exception e){
						System.out.println("pass");
					}
				}
			}
			cwMap.put("sectionId", temp.get("sectionId"));
			cwMap.put("block", cwGrid);
			cwMap.put("menu_nm", temp.get("blockTitle"));
			
			finalCwGrid.add(cwMap);
			cwMap = new HashMap<String, Object>();
			
		}
		
		
		return finalCwGrid;
	}
	
	
}
