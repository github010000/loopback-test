package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.rest.RestClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

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
	
	
	public Map<String, Object> cwGetGrid(String ver, Map<String, String> param) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		String type = CastUtil.getString(param.get("type"));
//		String type = (String)param.get("type");
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
		result.put("status_code", temp.get("status_code"));
		if(resultList != null) {
			result.put("grid", resultList);
			result.put("size", resultList.size()+"");
		}else {
			result = null;
		}
		return result;
		
	}
	
	public Map<String, Object> cwGetRelation(String ver, Map<String, String> param) {
		
		//기존 콘텐츠 아이디 추출
		String epsd_id = CastUtil.getString(param.get("epsd_id"));
//		String epsd_id = (String)param.get("epsd_id");
		param.put("itemType", "VIDEO_CONTENT");
		Map<String, Object> result = null;
		List resultList = null;

		String contentInfo = redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO,epsd_id);
		
		if(contentInfo != null) {
			result = new HashMap<String, Object>();
			
			
			//시리즈아이디, 에피소드아이디 추출로직
			String regex="\"epsd_rslu_id\"[\\s]*:[\\s]*\"([^\"]+)\"";
			String contentId = StrUtil.getRegexString(regex, contentInfo);
			param.put("con_id", contentId);
			
			
			
			
			String type = CastUtil.getString(param.get("type"));
//			String type = (String)param.get("type");
			
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
			
			if(temp == null) {
				return null;
			}
			
			String regexTitle = "\"sub_title\"[\\s]*:[\\s]*\"([^\"]+)\"";
			String contentTitle = StrUtil.getRegexString(regexTitle, contentInfo);
			temp.put("contentTitle", contentTitle);
			
			resultList = makeCwRelation(temp, epsd_id);
			result.put("status_code", temp.get("status_code"));
			if(resultList != null) {
				result.put("relation", resultList);
				result.put("size", resultList.size()+"");
			}else {
				//CW 연동로직에 데이터가 없을 경우 
				result.put("status_code", "0002");
				String srisInfo = redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO,epsd_id);
				String regexRelation = "\\\"relation_contents\\\"[\\s]*:[\\s]*(\\[\\{.*?\\}\\])";
				String relationData = StrUtil.getRegexString(regexRelation, srisInfo);
				if(relationData != null && !"".equals(relationData)) {
					resultList = CastUtil.StringToJsonList(relationData);
				}
				
				if(resultList!= null && !resultList.isEmpty()) {
					result.put("relation", resultList);
					result.put("size", resultList.size()+"");
				}else {
					result = null;
				}
			}
		}
		
		return result;
		
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
		
//		System.out.println(cwparam);
		String rest = null;
		rest = restClient.getRestUri(cwBaseUrl + path, cwUser, cwPassword, cwparam);

		//응답값 확인
		String restregex="\"code\"[\\s]*:[\\s]*([0-9]*)";
		String codeValue = StrUtil.getRegexString(restregex, rest);

		objMap = CastUtil.StringToJsonMap(rest);
		if("0".equals(codeValue)) {
			objMap.put("status_code", "0000");
			objMap.put("cw_call_id", param.get("cw_call_id"));
			objMap.put("epsd_id", param.get("epsd_id"));
		}else {
			objMap.put("status_code", "0002");
		}
		return objMap;
		
	}
	
	//CW 추천그리드 생성로직
	public List<Map<String, Object>> makeCwGrid(Map<String, Object> objMap) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);

		//데이터 가공로직 시작
		List<Map<String, Object>>cwGrid = new ArrayList<Map<String,Object>>();
		
		if(resultList != null && resultList.size()>0) {
			for(Map<String, Object>temp:resultList ) {
				List<Map<String, Object>>resultGridList = new ArrayList<Map<String,Object>>();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				List<String> tempIdList = CastUtil.getObjectToListString(temp.get("idList"));
				if(tempIdList != null) {
					for(String dataGrp:tempIdList) {
						String [] idNblockId = dataGrp.split("\\|");
						
						String epsd_rslu_id = idNblockId[0];
						String trackId = idNblockId[1];

						Map<String, Object> gridData = new HashMap<String, Object>();
						Map<String, Object> cidInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO,epsd_rslu_id));
						if(cidInfo != null) {
							String epsd_id = CastUtil.getObjectToString(cidInfo.get("epsd_id"));
							Map<String, Object> gridInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_CONTENTS_ITEM,epsd_id));
							
							if(gridInfo != null && !gridInfo.isEmpty()) {
							
								gridData.putAll(gridInfo);
								gridData.put("track_id", trackId);//? 뭘 써야할지...						
							
								resultGridList.add(gridData);
							}
						}
					}
				}
				resultMap.put("sectionId", temp.get("sectionId"));
				resultMap.put("session_id", objMap.get("sessionId"));
				resultMap.put("btrack_id", objMap.get("trackId"));
				resultMap.put("block", resultGridList);
				resultMap.put("sub_title", temp.get("blockTitle"));
				resultMap.put("block_cnt", resultGridList.size());
				resultMap.put("cw_call_id", objMap.get("cw_call_id"));

			
				cwGrid.add(resultMap);
				resultMap = new HashMap<String, Object>();
			
			}
		} else {
			cwGrid=null;
		}
		
		return cwGrid;
	}
	
	//CW 연관콘텐츠 생성로직
	public List makeCwRelation(Map<String, Object> objMap, String param_epsd_id) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);
		
		//데이터 가공로직 시작
		List cwRelation = null;

		int i = 0;
		if(resultList!=null && !resultList.isEmpty()) {
			cwRelation = new ArrayList<Map<String,Object>>();
			for(Map<String, Object>temp:resultList ) {
				List<Map<String, Object>>resultRelationList = new ArrayList<Map<String,Object>>();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				List<String> tempIdList = CastUtil.getObjectToListString(temp.get("idList"));
//				List<String>tempIdList = (List<String>) temp.get("idList");
				if(tempIdList != null && !tempIdList.isEmpty()) {
					for(String dataGrp:tempIdList) {
						String [] idNblockId = dataGrp.split("\\|");
						
						String epsd_rslu_id = idNblockId[0];
						String trackId = idNblockId[1];
						
						Map<String, Object> relationData = new HashMap<String, Object>();
						Map<String, Object> cidInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO,epsd_rslu_id));
						if(cidInfo!=null) {
							String epsd_id = CastUtil.getObjectToString(cidInfo.get("epsd_id"));
//							String epsd_id = (String) cidInfo.get("epsd_id");
//							String sris_id = (String) cidInfo.get("sris_id");
							Map<String, Object> contentInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_CONTENTS_ITEM,epsd_id));

//							Map<String, Object> contentInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS,sris_id));
//							Map<String, Object> srisInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO,epsd_id));
							if(contentInfo != null && !contentInfo.isEmpty()) {
								relationData.putAll(contentInfo);//? 뭘 써야할지...						
								relationData.put("track_id", trackId);						
								
								resultRelationList.add(relationData);
							}
						}
					}
				}else {
					//아이템이 없을 경우 카운트 추가
					i++;
				}
				String sub_title=CastUtil.getObjectToString(temp.get("blockTitle"));
				String content_title=CastUtil.getObjectToString(objMap.get("contentTitle"));
				if(sub_title != null && !sub_title.isEmpty()) {
					sub_title = restMatching(param_epsd_id, sub_title, content_title);
				}
				
				resultMap.put("sectionId", temp.get("sectionId"));
				resultMap.put("session_id", objMap.get("sessionId"));
				resultMap.put("btrack_id", objMap.get("trackId"));
				resultMap.put("block", resultRelationList);
				resultMap.put("t_cnt", resultRelationList.size()+"");
				resultMap.put("sub_title", sub_title);
				resultMap.put("cw_call_id", objMap.get("cw_call_id"));
				
				cwRelation.add(resultMap);
				resultMap = new HashMap<String, Object>();
			}
			
		}
		
		//CW status가 0 이었지만, 데이터가 하나도 없었을 경우 처리로직
		if(i==resultList.size()) {
			cwRelation = null;
		}
		
		return cwRelation;
	}
	
	//CW API에서 데이터 추출 (con_id등)
	public void getCwData(Map<String, Object> objMap, List<Map<String, Object>> resultList) {
		
		List<Map<String, Object>> sections = CastUtil.getObjectToMapList(objMap.get("sections"));
		
		//단일섹션은 응답값이 다르므로 담는 변수를 따로 둔다.
		List<Map<String, Object>> oneSecBlocks = CastUtil.getObjectToMapList(objMap.get("blocks"));
		

		
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		List<String> idList = new ArrayList<String>();
		
		
		if(sections != null) {
			for(Map<String, Object>sectionMap:sections) {
				
				List<Map<String, Object>> blocks = null;
				blocks = CastUtil.getObjectToMapList(sectionMap.get("blocks"));
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
					resultList.add(result);
					result = new HashMap<String, Object>();
				}else {	
					//멀티섹션(multi) 처리 로직
					makeItem(sectionMap, result, idList);
					idList = new ArrayList<String>();
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
	
	//중복로직 분리
	public void makeItem(Map<String, Object>temp, Map<String, Object> result, List<String> idList) {
		List<Map<String, Object>> items = null;
		items = CastUtil.getObjectToMapList(temp.get("items"));
		if(items != null) {
			for(Map<String, Object>itemMap:items) {
				Map<String, Object> itemIds = CastUtil.getObjectToMap(itemMap.get("itemIds")); 
				String contentId = CastUtil.getObjectToString(itemIds.get("CW"));
				String trackId = CastUtil.getObjectToString(itemMap.get("trackId"));
				idList.add(contentId+"|"+trackId);
			}
		}
		result.put("blockTitle",temp.get("blockLabel"));
		result.put("sectionId", temp.get("blockId"));
		result.put("sessionId", temp.get("sessionId"));
		result.put("btrackId", temp.get("trackId"));
		result.put("idList",idList);
	}	
	
	
	
	//연관콘텐츠 제목 생성로직
	public String restMatching(String epsd_id, String sub_title, String content_title) {
		
		String actorString = CastUtil.getObjectToString(redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, epsd_id));
		
		Map<String,List<Object>> codePeopleMap = null;
		if(actorString != null && !actorString.isEmpty()) {
			codePeopleMap = new HashMap<String, List<Object>>();
			codePeopleMap.put("Director", new ArrayList<Object>() );
			codePeopleMap.put("Actor", new ArrayList<Object>() );
			
			List<String> code = new ArrayList<String>();
			List<String> person = new ArrayList<String>();
			String regex="(prs_role_cd)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
			Pattern ptn = Pattern.compile(regex); 
			Matcher matcher = ptn.matcher(actorString); 
			while(matcher.find()){
				code.add(matcher.group(2));
			}
			
			String regexNm="(prs_nm)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
			Pattern ptnNm = Pattern.compile(regexNm); 
			Matcher matcherNm = ptnNm.matcher(actorString); 
			while(matcherNm.find()){
				person.add(matcherNm.group(2));
			}
			
//			System.out.println("RestTemplateSvc.restMatching() " + code.toString() + person.toString() ) ;
			
			
			// codePeopleMap 에 Director, Actor 별로 분기하여 List에 담기 위한 작업 loop
			int i = 0;
			for( String cdx : code ) {
				
				switch (cdx) {
					case "00": // Director
						codePeopleMap.get("Director").add(person.get(i));
						break;
						
					case "01": // Actor 
						codePeopleMap.get("Actor").add(person.get(i));
						break;
		
					default:
						codePeopleMap.get("Director").add(person.get(i));
						break;
				}
				
				i++;
			}
		}
		return repalceData( sub_title, codePeopleMap, content_title);
		
	}
	
	private String repalceData( String man, Map<String,List<Object>> codePeopleMap, String content_title ) {
		
		if(man != null && !man.isEmpty()) {
			man = man.replaceAll("\\#", ""); // # 없애버리기 
			if( man.startsWith("Actor") ) {  // Actor로 시작하는거
				return getReturnData( man, "Actor", codePeopleMap);
				
			}else if( man.startsWith("Director") ) { // Director로 시작하는거
				return getReturnData( man, "Director", codePeopleMap);
			}else if( man.startsWith("Collection") ) {// Collection으로 시작하는거
				return man.replaceAll("Collection", content_title);
			}else {
				return man;
			}
		}
		return "연관 콘텐츠";
	}
	
	public String getReturnData(String man, String type, Map<String,List<Object>> codePeopleMap  ) {
	
		if(codePeopleMap!=null && !codePeopleMap.isEmpty()) {
			String pos = StringUtils.defaultIfEmpty( man.split(" ")[0].replace(type, ""), "" );
			int cnt = Integer.parseInt( pos.equals("") ? "1" : pos );
			return man.replace(type+pos, codePeopleMap.get(type).get(cnt-1).toString() );
		}else {
			return "연관콘텐츠";
		}
	}


}
