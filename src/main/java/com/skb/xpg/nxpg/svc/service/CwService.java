package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = null;

		String contentInfo = redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO,epsd_id);
		
		if(contentInfo != null) {
			
			
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
			
			resultList = makeCwRelation(temp, epsd_id);
			result.put("status_code", temp.get("status_code"));
			if(resultList != null) {
				result.put("relation", resultList);
				result.put("size", resultList.size()+"");
			}else {
				result = null;
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
				List<String>tempIdList = (List<String>) temp.get("idList");
				if(tempIdList != null) {
					for(String dataGrp:tempIdList) {
						String [] idNblockId = dataGrp.split("\\|");
						
						String epsd_rslu_id = idNblockId[0];
						String trackId = idNblockId[1];
						
						Map<String, Object> gridData = new HashMap<String, Object>();
						Map<String, Object> cidInfo = CastUtil.StringToJsonMap(redisClient.hget("contents_cidinfo",epsd_rslu_id));
						if(cidInfo != null) {
							String epsd_id = CastUtil.getObjectToString(cidInfo.get("epsd_id"));
							String sris_id = CastUtil.getObjectToString(cidInfo.get("sris_id"));
//							String epsd_id = (String) cidInfo.get("epsd_id");
//							String sris_id = (String) cidInfo.get("sris_id");
							
							Map<String, Object> contentInfo = CastUtil.StringToJsonMap(redisClient.hget("synopsis_contents",sris_id));
							Map<String, Object> srisInfo = CastUtil.StringToJsonMap(redisClient.hget("synopsis_srisInfo",epsd_id));
							if(contentInfo != null && srisInfo != null) {
								
								gridData.put("poster_filename_h", srisInfo.get("epsd_poster_filename_h"));
								gridData.put("sris_id", srisInfo.get("sris_id"));
								gridData.put("poster_filename_v", srisInfo.get("epsd_poster_filename_v"));
								gridData.put("sris_nm", contentInfo.get("title"));
								gridData.put("epsd_id", srisInfo.get("epsd_id"));
								gridData.put("adlt_lvl_cd", srisInfo.get("adlt_lvl_cd"));
								gridData.put("title", srisInfo.get("sub_title"));//? 뭘 써야할지...						
								gridData.put("trackId", trackId);//? 뭘 써야할지...						
								
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
		}else {
			cwGrid=null;
		}
		
		
		return cwGrid;
	}
	
	public List makeCwRelation(Map<String, Object> objMap, String param_epsd_id) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);
		
		//데이터 가공로직 시작
		List cwRelation = null;
		
		//TODO 진입한 콘텐츠의 인물정보 가져오기
//		String prnInfo = (String)redisClient.hget("contents_people","CE0001270830");
//		
//		String regex="(peoples)\"[\\s]*:[\\s]*(.*)\\]";
////		String regex="(peoples)\"[\\s]*:[\\s]*\\[(.*)\\]";
//		Pattern ptn = Pattern.compile(regex); 
//		Matcher matcher = ptn.matcher(prnInfo); 
//		while(matcher.find()){
//			System.out.println(matcher.group(2));
//		}
//		
//		System.out.println(resultList);
		if(resultList!=null && resultList.size()>0) {
			cwRelation = new ArrayList<Map<String,Object>>();
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
						Map<String, Object> cidInfo = CastUtil.StringToJsonMap(redisClient.hget("contents_cidinfo",epsd_rslu_id));
						if(cidInfo!=null) {
							String epsd_id = CastUtil.getObjectToString(cidInfo.get("epsd_id"));
							String sris_id = CastUtil.getObjectToString(cidInfo.get("sris_id"));
//							String epsd_id = (String) cidInfo.get("epsd_id");
//							String sris_id = (String) cidInfo.get("sris_id");
							
							Map<String, Object> contentInfo = CastUtil.StringToJsonMap(redisClient.hget("synopsis_contents",sris_id));
							Map<String, Object> srisInfo = CastUtil.StringToJsonMap(redisClient.hget("synopsis_srisInfo",epsd_id));
							if(contentInfo != null && srisInfo != null) {
								relationData.put("poster_filename_h", srisInfo.get("epsd_poster_filename_h"));
								relationData.put("sris_id", srisInfo.get("sris_id"));
								relationData.put("poster_filename_v", srisInfo.get("epsd_poster_filename_v"));
								relationData.put("sris_nm", contentInfo.get("title"));
								relationData.put("epsd_id", srisInfo.get("epsd_id"));
								relationData.put("adlt_lvl_cd", srisInfo.get("adlt_lvl_cd"));
								relationData.put("title", srisInfo.get("sub_title"));//? 뭘 써야할지...						
								relationData.put("track_id", trackId);						
								
								resultRelationList.add(relationData);
							}
						}
					}
				}
	//			makeRelationTitle((String) temp.get("blockTitle"), (String)objMap.get("epsd_id"));
				
				resultMap.put("sectionId", temp.get("sectionId"));
				resultMap.put("session_id", objMap.get("sessionId"));
				resultMap.put("btrack_id", objMap.get("trackId"));
				resultMap.put("block", resultRelationList);
				resultMap.put("t_cnt", resultRelationList.size()+"");
				resultMap.put("sub_title", temp.get("blockTitle"));
				resultMap.put("cw_call_id", objMap.get("cw_call_id"));
				
				cwRelation.add(resultMap);
				resultMap = new HashMap<String, Object>();
			}
		}else {
			
			String srisInfo = redisClient.hget("synopsis_srisInfo",param_epsd_id);
			String regexRelation = "\\\"relation_contents\\\"[\\s]*:[\\s]*(\\[\\{.*?\\}\\])";
			String relationData = StrUtil.getRegexString(regexRelation, srisInfo);
			if(relationData != null && !"".equals(relationData)) {
				cwRelation = CastUtil.StringToJsonList(relationData);
			}
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
	
	public void makeItem(Map<String, Object>temp, Map<String, Object> result, List<String> idList) {
		List<Map<String, Object>> items = null;
		items = (List<Map<String, Object>>) temp.get("items");
		if(items != null) {
			for(Map<String, Object>itemMap:items) {
				Map<String, Object> itemIds = (Map<String, Object>) itemMap.get("itemIds");
				String contentId = CastUtil.getObjectToString(itemIds.get("CW"));
				String trackId = CastUtil.getObjectToString(itemIds.get("trackId"));
//				String contentId = (String) itemIds.get("CW");
//				String trackId = (String) itemMap.get("trackId");
				idList.add(contentId+"|"+trackId);
			}
		}
		result.put("blockTitle",temp.get("blockLabel"));
		result.put("sectionId", temp.get("blockId"));
		result.put("sessionId", temp.get("sessionId"));
		result.put("btrackId", temp.get("trackId"));
		result.put("idList",idList);
	}	
	
	
	
//	public void  restMatching() {
//		
//		
//		Map<String,List<Object>> codePeopleMap = new HashMap<String, List<Object>>();
//		codePeopleMap.put(DIRECTOR, new ArrayList<Object>() );
//		codePeopleMap.put(ACTOR, new ArrayList<Object>() );
//		
//		List<String> code = new ArrayList<String>();
//		List<String> person = new ArrayList<String>();
//		
//		String regex="(prs_role_cd)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
//		Pattern ptn = Pattern.compile(regex); 
//		Matcher matcher = ptn.matcher(/*actorString 배우 및 감독*/); 
//		while(matcher.find()){
//			code.add(matcher.group(2));
//		}
//		
//		String regexNm="(prs_nm)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
//		Pattern ptnNm = Pattern.compile(regexNm); 
//		Matcher matcherNm = ptnNm.matcher(/*actorString 배우 및 감독*/); 
//		while(matcherNm.find()){
//			person.add(matcherNm.group(2));
//		}
//		
//		System.out.println("RestTemplateSvc.restMatching() " + code.toString() + person.toString() ) ;
//		
//		
//		// codePeopleMap 에 Director, Actor 별로 분기하여 List에 담기 위한 작업 loop
//		int i = 0;
//		for( String cdx : code ) {
//			
//			switch (cdx) {
//				case "00": // Director
//					codePeopleMap.get(DIRECTOR).add(person.get(i));
//					break;
//					
//				case "10": // Actor 
//					codePeopleMap.get(ACTOR).add(person.get(i));
//					break;
//	
//				default:
//					codePeopleMap.get(DIRECTOR).add(person.get(i));
//					break;
//			}
//			
//			i++;
//		}
//		
//		String[] peoples = {"#Actor1# 출연 영화","#Director2# 연출 영화","#Actor2# 출연 영화","#Director1# 연출 영화"};
//		
//		// people ["#Actor1# 출연 영화","#Director2# 연출 영화","#Actor2# 출연 영화","#Director1# 연출 영화"];
//		for( String man: peoples) {
//			System.out.println( repalceData( man, codePeopleMap) );
//		}
//		
//	}
//	
//	private String repalceData( String man, Map<String,List<Object>> codePeopleMap ) {
//		
//		man = man.replaceAll("\\#", ""); // # 없애버리기 
//		
//		if( man.startsWith(ACTOR) ) {  // Actor로 시작하는거
//			return getReturnData( man, ACTOR, codePeopleMap);
//			
//		}else if( man.startsWith(DIRECTOR) ) { // Director로 시작하는거
//			return getReturnData( man, DIRECTOR, codePeopleMap);
//		}
//		return "";
//	}
//	
//	public String getReturnData(String man, String type, Map<String,List<Object>> codePeopleMap  ) {
//	
//		String pos = StringUtils.defaultIfEmpty( man.split(" ")[0].replace(type, ""), "" );
//		int cnt = Integer.parseInt( pos.equals("") ? "1" : pos );
//		return man.replace(type+pos, codePeopleMap.get(type).get(cnt-1).toString() );
//	}
//	
//	public String makeRelationTitle(String blockTitle, String epsd_id) {
//
////		String titleKey = "";
////		String subTitle = "";
////
////		List<String> tokenList = new ArrayList<String>();
////		
////		//연관콘텐츠 제목 추출
////		StringTokenizer tokens = new StringTokenizer(blockTitle, "#");
////		
////		while(tokens.hasMoreTokens()){
////			tokenList.add(tokens.nextToken());
////		}
////		
////
////		titleKey = tokenList.get(0);		//#으로 붙어있는 단어들중 맨 앞단어를 사용
////		titleKey = titleKey.toUpperCase();
////		
////		if(tokenList.size()>1) {
////			subTitle = tokenList.get(1);
////		}
//		
//		
//		String prnInfo = (String) redisClient.hget("contents_people","CE0001270830");
//
//		List<String> code = new ArrayList<String>();
//		List<String> person = new ArrayList<String>();
//		
//		
//		String aa = "#Director# 출연 영화";
//		int i = 2;
//		
//		
//		
////		[00,00,10,10,10,10]
////		[ccc,aaa,ddd,cc,vvv,nnn]
//				
//		String regex="(prs_role_cd)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
//		Pattern ptn = Pattern.compile(regex); 
//		Matcher matcher = ptn.matcher(prnInfo); 
//		while(matcher.find()){
//			code.add(matcher.group(2));
//		}
//		
//		String regexNm="(prs_nm)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
//		Pattern ptnNm = Pattern.compile(regexNm); 
//		Matcher matcherNm = ptnNm.matcher(prnInfo); 
//		while(matcherNm.find()){
//			person.add(matcherNm.group(2));
//		}
//		
//		
////		int actIdx  = 0;
////		int dicIdx = 0;
////		
////		for( String str : code  ) {
////			
////			if ( "")
////			
////			aa.replace("Actor"+String.valueOf(i), person.get(i) );
////		}
//		
//		
//		
//		return "";
//				
//		
//		
//	}
}
