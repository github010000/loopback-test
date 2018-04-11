package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			default:
				temp = cwCall("getonepage", param);
				break;
		}
		
		resultList = makeCwGrid(temp);
		
		return resultList;
		
	}
	
	public List<Map<String, Object>> cwGetRelation(String ver, Map<String, String> param) {
		
		//기존 콘텐츠 아이디 추출
		String epsd_id = (String)param.get("epsd_id");
		param.put("itemType", "VIDEO_CONTENT");
		
		String contentInfo = (String) redisClient.hget("synopsis_srisInfo",epsd_id);
		
		//시리즈아이디, 에피소드아이디 추출로직
		String regex="\"epsd_rslu_id\"[\\s]*:[\\s]*\"([^\"]+)\"";
		Pattern ptn = Pattern.compile(regex); 
		Matcher matcher = ptn.matcher(contentInfo); 
		String contentId  = "";
		while(matcher.find()){ 
			contentId=matcher.group(1);
			break;
		}
		param.put("con_id", contentId);

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
			default:
				temp = cwCall("getonepage", param);
				break;
		}
		
		resultList = makeCwRelation(temp);
		
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
		//파라미터에 콘텐츠 아이디가 존재하면 연관콘텐츠 호출
		if(param.containsKey("con_id")) {
			param.put("item", param.remove("con_id"));
			requestparam = requestparam.replaceAll(regex, "item");
			nxpgparam = nxpgparam.replaceAll(regex, "item");
		}else {
			param.put("user", param.remove("stb_id"));
			requestparam = requestparam.replaceAll(regex, "user");
			nxpgparam = nxpgparam.replaceAll(regex, "user");
		}
		
		
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
		
		objMap.put("cw_call_id", param.get("cw_call_id"));
		objMap.put("epsd_id", param.get("epsd_id"));
		
		return objMap;
	}
	
	
	public List<Map<String, Object>> makeCwGrid(Map<String, Object> objMap) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);

		System.out.println("::::::"+resultList);
		
		//데이터 가공로직 시작
		List<Map<String, Object>>cwGrid = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object>temp:resultList ) {
			List<Map<String, Object>>resultGridList = new ArrayList<Map<String,Object>>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<String>tempIdList = (List<String>) temp.get("idList");
			if(tempIdList != null) {
				for(String epsd_rslu_id:tempIdList) {
					Map<String, Object> gridData = new HashMap<String, Object>();
					Map<String, Object> cidInfo = CastUtil.StringToJsonMap((String) redisClient.hget("contents_cidinfo",epsd_rslu_id));
					try {
						String epsd_id = (String) cidInfo.get("epsd_id");
						String sris_id = (String) cidInfo.get("sris_id");
						
						Map<String, Object> contentInfo = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_contents",sris_id));
						Map<String, Object> srisInfo = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_srisInfo",epsd_id));
						
						gridData.put("poster_filename_h", srisInfo.get("epsd_poster_filename_h"));
						gridData.put("sris_id", srisInfo.get("sris_id"));
						gridData.put("poster_filename_v", srisInfo.get("epsd_poster_filename_v"));
						gridData.put("sris_nm", contentInfo.get("title"));
						gridData.put("epsd_id", srisInfo.get("epsd_id"));
						gridData.put("adlt_lvl_cd", srisInfo.get("adlt_lvl_cd"));
						gridData.put("title", srisInfo.get("sub_title"));//? 뭘 써야할지...						
						
						resultGridList.add(gridData);
						
					}catch (Exception e){
						System.out.println("pass");
					}
				}
			}
			resultMap.put("sectionId", temp.get("sectionId"));
			resultMap.put("block", resultGridList);
			resultMap.put("sub_title", temp.get("blockTitle"));
			resultMap.put("status_code", objMap.get("status_code"));
			
			cwGrid.add(resultMap);
			resultMap = new HashMap<String, Object>();
			
		}
		
		
		return cwGrid;
	}
	
	public List<Map<String, Object>> makeCwRelation(Map<String, Object> objMap) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);
		
		System.out.println("::::::"+resultList);
		
		//데이터 가공로직 시작
		List<Map<String, Object>>cwRelation = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object>temp:resultList ) {
			List<Map<String, Object>>resultRelationList = new ArrayList<Map<String,Object>>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<String>tempIdList = (List<String>) temp.get("idList");
			if(tempIdList != null) {
				for(String dataGrp:tempIdList) {
					String [] idNblockId = dataGrp.split("\\|");
					
					String epsd_rslu_id = idNblockId[0];
					String trackId = idNblockId[1];
					
					Map<String, Object> relationData = new HashMap<String, Object>();
					Map<String, Object> cidInfo = CastUtil.StringToJsonMap((String) redisClient.hget("contents_cidinfo",epsd_rslu_id));
					try {
						String epsd_id = (String) cidInfo.get("epsd_id");
						String sris_id = (String) cidInfo.get("sris_id");
						
						Map<String, Object> contentInfo = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_contents",sris_id));
						Map<String, Object> srisInfo = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_srisInfo",epsd_id));
						
						relationData.put("poster_filename_h", srisInfo.get("epsd_poster_filename_h"));
						relationData.put("sris_id", srisInfo.get("sris_id"));
						relationData.put("poster_filename_v", srisInfo.get("epsd_poster_filename_v"));
						relationData.put("sris_nm", contentInfo.get("title"));
						relationData.put("epsd_id", srisInfo.get("epsd_id"));
						relationData.put("adlt_lvl_cd", srisInfo.get("adlt_lvl_cd"));
						relationData.put("title", srisInfo.get("sub_title"));//? 뭘 써야할지...						
						relationData.put("track_id", trackId);						
						
						resultRelationList.add(relationData);
						
					}catch (Exception e){
						System.out.println("pass");
					}
				}
			}
//			makeRelationTitle((String) temp.get("blockTitle"), (String)objMap.get("epsd_id"));
			
			resultMap.put("sectionId", temp.get("sectionId"));
			resultMap.put("session_id", temp.get("sessionId"));
			resultMap.put("btrack_id", temp.get("btrackId"));
			resultMap.put("block", resultRelationList);
			resultMap.put("t_cnt", resultRelationList.size()+"");
			resultMap.put("sub_title", temp.get("blockTitle"));
			resultMap.put("cw_call_id", objMap.get("cw_call_id"));
			
			cwRelation.add(resultMap);
			resultMap = new HashMap<String, Object>();
			
		}
		
		
		return cwRelation;
	}
	
	
	public void getCwData(Map<String, Object> objMap, List<Map<String, Object>> resultList) {
		
		List<Map<String, Object>> sections = (List<Map<String, Object>>) objMap.get("sections");
		
		//단일섹션은 응답값이 다르므로 담는 변수를 따로 둔다.
		List<Map<String, Object>> oneSecBlocks = (List<Map<String, Object>>) objMap.get("blocks");
		
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		List<String> idList = new ArrayList<String>();
		
		if(sections != null) {
			for(Map<String, Object>sectionMap:sections) {
				List<Map<String, Object>> blocks = null;
				blocks = (List<Map<String, Object>>) sectionMap.get("blocks");
				if(blocks != null) {
					for(Map<String, Object>blockMap:blocks) {
						//데이터 생성 로직
						makeItem(blockMap, result, idList);
						idList = new ArrayList<String>();
					}
				}
				//응답값에 sectionId가 없으면 multisection이 호출된 것이다.
				if(sectionMap.get("sectionId")!=null) {
					//전체 섹션(all), 단일page(onepage) 처리 로직
					result.put("sectionId", sectionMap.get("sectionId"));
					idList = new ArrayList<String>();
					resultList.add(result);
					result = new HashMap<String, Object>();
				}else {	
					//멀티섹션(multi) 처리 로직
					makeItem(sectionMap, result, idList);
					resultList.add(result);
					result = new HashMap<String, Object>();
				}
			}
		}else if(oneSecBlocks != null) {
			//단일section(onesection) 처리 로직
			for(Map<String, Object>temp:oneSecBlocks) {
				makeItem(temp, result, idList);
				idList = new ArrayList<String>();
				resultList.add(result);
				result = new HashMap<String, Object>();
			}
		}
	}
	
	public void makeItem(Map<String, Object>temp, Map<String, Object> result, List<String> idList) {
		List<Map<String, Object>> items = null;
		items = (List<Map<String, Object>>) temp.get("items");
		if(items != null) {
			for(Map<String, Object>itemMap:items) {
				Map<String, Object> itemIds = (Map<String, Object>) itemMap.get("itemIds");
				String contentId = (String) itemIds.get("CW");
				String trackId = (String) itemMap.get("trackId");
				idList.add(contentId+"|"+trackId);
			}
		}
		result.put("blockTitle",temp.get("blockLabel"));
		result.put("sectionId", temp.get("blockId"));
		result.put("sessionId", temp.get("sessionId"));
		result.put("btrackId", temp.get("trackId"));
		result.put("idList",idList);
	}	
	
//	public void makeRelationTitle(String blockTitle, String epsd_id) {
//
//		String titleKey = "";
//		String subTitle = "";
//
//		List<String> tokenList = new ArrayList<String>();
//		
//		//연관콘텐츠 제목 추출
//		StringTokenizer tokens = new StringTokenizer(blockTitle, "#");
//		
//		while(tokens.hasMoreTokens()){
//			tokenList.add(tokens.nextToken());
//		}
//		
//
//		titleKey = tokenList.get(0);		//#으로 붙어있는 단어들중 맨 앞단어를 사용
//		titleKey = titleKey.toUpperCase();
//		
//		if(tokenList.size()>1) {
//			subTitle = tokenList.get(1);
//		}
//		
//		
//		String prnInfo = (String) redisClient.hget("contents_people",epsd_id);
//
//		List<String> director = new ArrayList<String>();
//		
//		String regex="\"prs_role_nm\"[\\s]*:[\\s]*\"([^\"]+)\"";
//		Pattern ptn = Pattern.compile(regex); 
//		Matcher matcher = ptn.matcher(prnInfo); 
//		String job  = "";
//		while(matcher.find()){ 
//			job = matcher.group(1);
//			if(job.contains("감독")) {
//				director.add()
//			}else {
//				
//			}
//			
//		}
//		
//	}
}
