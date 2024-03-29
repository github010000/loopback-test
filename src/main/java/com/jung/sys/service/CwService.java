package com.jung.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jung.sys.common.NXPGCommon;
import com.jung.sys.config.Properties;
import com.jung.sys.redis.RedisClient;
import com.jung.sys.rest.RestClient;
import com.jung.sys.util.CastUtil;
import com.jung.sys.util.LogUtil;
import com.jung.sys.util.StrUtil;

@Service
@RefreshScope
public class CwService {
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private Properties properties;
	
	@Autowired
	private RestClient restClient;

	
//	@Autowired
//	@Qualifier("cwBaseUrl")
//	private String cwBaseUrl;
	@Value("${user.cw.baseurl}")
	private String cwBaseUrl;

//	@Value("${user.cwswitch}")
//	private boolean cwSwitch;
	
	@Autowired
	@Qualifier("cwUser")
	private String cwUser;
	
	@Autowired
	@Qualifier("cwPassword")
	private String cwPassword;
	
	@Autowired
	private GridService gridService;
	
	@Autowired
	private MakeCWService makeCWService;
	
	//19.01.29 추가
	@Value("${user.cw.conntimeout}")
	private int conntimeout;

	@Value("${user.cw.biztimeout}")
	private int biztimeout;
	
	public Map<String, Object> cwGetGrid(String ver, Map<String, String> param) {

		try {
			param.put("cw_stb_id", param.get("stb_id"));
			
			Map<String, Object> result = new HashMap<String, Object>();
			String type = CastUtil.getString(param.get("type"));
	//		String type = (String)param.get("type");
			List<Map<String, Object>> resultList = null;
			Map<String, Object> temp = null;
			
			if(NXPGCommon.cwSwitch) {
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
					temp = cwCall("fullsection", param);
					break;
				}
			}
			
			Map<String, Object> time = null;
			
			if(temp != null) {
				time = CastUtil.getObjectToMap(temp.get("time"));
				if (time != null) {
					time.put("after_start", System.nanoTime());
				}
				
				temp.put("type", type);
				resultList = makeCwGrid(temp, param, time);
				result.put("status_code", temp.get("status_code"));
				if(resultList != null && resultList.size()>0) {
					result.put("grid", resultList);
					result.put("size", resultList.size()+"");
				}else {
					LogUtil.info("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "CW", "CW Data is null or item list size is 0");
					result = null;
				}
				//Log에 처리 시간 프린트 (CW포함)
				if (time != null) {
					time.put("after_end", System.nanoTime());
				}
//				printProcessTime(time, param);
			}else {
				LogUtil.info("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "CW", "CW API return null. switch value: " + NXPGCommon.cwSwitch);
				result = null;
			}
			
			return result;
			
		} catch (Exception e) {
			LogUtil.error("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "", e.toString());
			LogUtil.error("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[1].toString());
			LogUtil.error("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[2].toString());
			LogUtil.error("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[3].toString());
			LogUtil.error("IF-NXPG-009", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[4].toString());
			return null;
		}
	}
	
	public Map<String, Object> cwGetRelation(String ver, Map<String, String> param) {
		
		try {
			//기존 콘텐츠 아이디 추출
			String epsd_id = CastUtil.getString(param.get("epsd_id"));
			String epsd_rslu_id = CastUtil.getString(param.get("epsd_rslu_id"));
			String cw_call_id = CastUtil.getString(param.get("cw_call_id"));
//			String epsd_id = (String)param.get("epsd_id");
			param.put("itemType", "VIDEO_CONTENT");
			Map<String, Object> result = null;
			List resultList = null;
			Map<String, Object> time = null;
			Map<String, Object> temp = null;
			
			String contentInfo = redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id, param);

			if(contentInfo != null) {
				result = new HashMap<String, Object>();
				
				//에피소드아이디 추출로직
				String regex="\"epsd_rslu_id\"[\\s]*:[\\s]*\"([^\"]+)\"";
				String contentId = StrUtil.getRegexString(regex, contentInfo);

				//시리즈 아이디 추출 로직
				String regexSris="\"sris_id\"[\\s]*:[\\s]*\"([^\"]+)\"";
				String srisId = StrUtil.getRegexString(regexSris, contentInfo);
				
				if(epsd_rslu_id != null && !epsd_rslu_id.isEmpty()) {
					param.put("con_id", epsd_rslu_id);
				}else {
					param.put("con_id", contentId);
				}
				
				
				String type = CastUtil.getString(param.get("type"));
//				String type = (String)param.get("type");
				
				if(NXPGCommon.cwSwitch) {
					//cw_call_id가 안들어오면 CW는 호출하지 않는다.
					if(cw_call_id != null && !cw_call_id.isEmpty()) {
						
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
							temp = cwCall("fullsection", param);
							break;
						}
					}
				}
				
				//파라미터가 안넘어 왔거나 temp값이 없을 시 처리
				if(temp == null) {
					
					if(result == null) return result = null;
					LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "CW API return null switch value: " + NXPGCommon.cwSwitch);
					result.put("status_code", "0002");
					String srisInfo = redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id, param);
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
					
					return result;
				}else {
					time = CastUtil.getObjectToMap(temp.get("time"));
					if (time != null) {
						time.put("after_start", System.nanoTime());
					}
//					if(result == null) return null;
					temp.put("type", type);
					
					
					
					String contentTitleInfo = redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, srisId, param);
					
					
					String regexTitle = "\"title\"[\\s]*:[\\s]*\"([^\"]+)\"";
					String contentTitle = StrUtil.getRegexString(regexTitle, contentTitleInfo);
					temp.put("contentTitle", contentTitle);
					
					resultList = makeCwRelation(temp, epsd_id, param, time);
					
					if(result != null && resultList != null && resultList.size()>0 && temp.get("status_code") != null) {
						result.put("status_code", temp.get("status_code"));
						result.put("relation", resultList);
						result.put("size", resultList.size()+"");
					}else {
						result.put("status_code", temp.get("status_code"));
						//CW 연동로직에 데이터가 없을 경우 
						LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "CW Data is null or item list size is 0");
						result.put("status_code", "0002");
						String srisInfo = redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO,epsd_id, param);
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
					if (time != null) {
						time.put("after_end", System.nanoTime());
					}
//					printProcessTime(time, param);
				}
			}else {
				LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "NCMS", "content data is null. epsd_id: "+epsd_id);
			}
			
			//Log에 처리 시간 프린트 (CW포함)
			
			return result;
		} catch (Exception e) {
			LogUtil.error("", "", "", "", "", e.toString());
			LogUtil.error("IF-NXPG-012", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[1].toString());
			LogUtil.error("IF-NXPG-012", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[2].toString());
			LogUtil.error("IF-NXPG-012", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[3].toString());
			LogUtil.error("IF-NXPG-012", "", param.get("UUID"), param.get("cw_stb_id"), "", e.getStackTrace()[4].toString());
			return null;
		}
		
		
		
	}
	
	//API 호출 메소드
	public Map<String, Object> cwCall(String type, Map<String, String> param){

		Map<String, Object> objMap = null;
		Map<String, Object> cw = properties.getCw();
		Map<String, Object> keyAndValue;
		
		String cwparam = "";
		
		if (cw == null) return null;
		
		keyAndValue = CastUtil.getObjectToMap(cw.get(type));
		
		if(keyAndValue == null) return null;
		
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
		
		if (arrRequestParam != null && arrNxpgParam != null) {
			for (int a = 0; a < arrNxpgParam.length; a++) {
				//itemType값이 null일 경우 파라미터에 추가하지 않음.
				if (!arrRequestParam[a].isEmpty()) {
//					if(!"itemType".equals(arrRequestParam[a])) {
//						cwparam += ";" + arrRequestParam[a] + "|" + param.get(arrNxpgParam[a]);
//					}else {
						if("null".equals(arrNxpgParam[a])) {
							arrNxpgParam[a] = "";
						}
						if(param.get(arrNxpgParam[a])!=null && !"null".equals(arrNxpgParam[a])) {
							cwparam += ";" + arrRequestParam[a] + "|" + param.get(arrNxpgParam[a]);
						}
//					}
				}
			}
		}
		if (cwparam!=null && !cwparam.isEmpty() && cwparam.length() > 1) {
			cwparam = cwparam.substring(1);
		}
		
		Map<String, Object> restResult = null;
		String rest = null;
		long before = System.nanoTime();
		restResult = restClient.getRestUri(cwBaseUrl + path, cwUser, cwPassword, cwparam, param);
		
		param.put("cw_time_start", before + "");
		param.put("cw_time_end", System.nanoTime() + "");
//		restResult.put("url", cwBaseUrl + path);
		
		if (restResult != null && restResult.containsKey("result")) {
			rest = restResult.get("result") + "";
		}
		
		if(rest != null && !rest.isEmpty()) {
			//응답값 확인
			String restregex="\"code\"[\\s]*:[\\s]*([0-9]*)";
			String codeValue = StrUtil.getRegexString(restregex, rest);
			String genretitle = "";

			//장르 추출
			//JSON Parsing
			
			boolean checkSection = StrUtil.checkField(rest, "$.sections[0].blocks[0].items[0].fields.GENRESIMILARARRAY[0]");
			boolean checkBlock = StrUtil.checkField(rest, "$.blocks[0].items[0].fields.GENRESIMILARARRAY[0]");
			
			ReadContext ctx = JsonPath.parse(rest);
			
			if((checkSection || checkBlock) && ctx != null) {
				//title Search
				String genreSimilar = "";
				if (checkSection) {
					genreSimilar = ctx.read("$.sections[0].blocks[0].items[0].fields.GENRESIMILARARRAY[0]");
				} else {
					genreSimilar = ctx.read("$.blocks[0].items[0].fields.GENRESIMILARARRAY[0]");
				}
				if (genreSimilar != null) {
					String title = genreSimilar;
					
					title=title.substring(1, title.length()-1);
					List<String> firstGenreTitleList = Arrays.asList(title.split("#"));
					
					int y = 1;
					if (firstGenreTitleList.size() > 1) {
						y = 2;
					}
					
					for(int i = 0 ; i < y; i++) {
						if (firstGenreTitleList.get(i) != null) {
							genretitle += firstGenreTitleList.get(i)+",";
						}
					}
					
					if (genretitle.length() > 0) {
						genretitle = genretitle.substring(0, genretitle.length()-1);
					}
				}
			}//장르추출 끝
			
			objMap = CastUtil.StringToJsonMap(rest);
			if(objMap!=null && !objMap.isEmpty()) {
				if("0".equals(codeValue)) {
					objMap.put("status_code", "0000");
					objMap.put("cw_call_id", param.get("cw_call_id"));
					objMap.put("epsd_id", param.get("epsd_id"));
					param.put("genreList", genretitle);
				}else {
					objMap.put("status_code", "0002");
//					LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "CW code : "+codeValue+", url : " + cwBaseUrl + path);
				}
				
				if (restResult != null) {
					restResult.remove("result");
					objMap.put("time", restResult);
				}
				
			}
		} else {
//			LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "rest data null, url : " + cwBaseUrl + path);
		}
		
		
		return objMap;
		
	}
	
	//CW 추천그리드 생성로직
	public List<Map<String, Object>> makeCwGrid(Map<String, Object> objMap, Map<String, String> param, Map<String, Object> time) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		String type = CastUtil.getObjectToString(objMap.get("type"));
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);
		
		List<Map<String, Object>>resultGrid = new ArrayList<Map<String,Object>>();
		//데이터 가공로직 시작
		ExecutorService threadPool = Executors.newCachedThreadPool();
		FutureTask<List<Map<String, Object>>> task = new FutureTask<List<Map<String, Object>>>(new Callable<List<Map<String, Object>>>() {
			public List<Map<String, Object>> call() throws Exception {
				param.put("biz_thread_time_start", System.nanoTime() + "");
				
				List<Map<String, Object>>cwGrid = new ArrayList<Map<String,Object>>();
		
				int i = 0;
				int cnt = 0;
				boolean checkFirstBlock = true;
				
				if (resultList != null && resultList.size() > 0) {
					for (Map<String, Object> temp : resultList) {
						List<Map<String, Object>>resultGridList = new ArrayList<Map<String,Object>>();
						Map<String, Object> resultMap = new HashMap<String, Object>();
						List<String> tempIdList = CastUtil.getObjectToListString(temp.get("idList"));
						
						if(tempIdList != null) {
							List<Map<String, Object>> makedList = new ArrayList<Map<String, Object>>();
							for(String dataGrp:tempIdList) {
								String [] idNblockId = dataGrp.split("\\|");
		
								Map<String, Object> gridData = new HashMap<String, Object>();
									
								makedList = makeCWService.makeCwGridMap(idNblockId, gridData, param);
								if (makedList.size() > 0) {
									resultGridList.add(makedList.get(0));
								}
								cnt++;
								
							}
//							for (Map<String, Object> makedMap : makedList) {
//						        	resultGridList.add(makedMap);
//						    }
							
//							time.put("redis_count", cnt);
						} else {
							//아이템이 없을 경우 카운트 추가
							if(type != null && !"section".equals(type)) {
								i++;
							}
						}
		
						//onepage호출시 첫번쨰 블록에 데이터가 없으면 리턴
						if(type != null && "onepage".equals(type) && checkFirstBlock && resultGridList.size()<=0) {
							LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "No Exist Match Data. CW code: 0, CW result size : " +  + resultList.size());
							checkFirstBlock=false;
							return null;
						}
						
						resultMap.put("sectionId", temp.get("sectionId"));
						resultMap.put("session_id", objMap.get("sessionId"));
						if(objMap.containsKey("trackId") && objMap.get("trackId")!= null && !"".equals(objMap.get("trackId"))) {
							resultMap.put("btrack_id", objMap.get("trackId"));
						}else {
							resultMap.put("btrack_id", temp.get("btrackId"));
						}
						resultMap.put("block", resultGridList);
						resultMap.put("sub_title", temp.get("blockTitle"));
						resultMap.put("block_cnt", resultGridList.size());
						resultMap.put("cw_call_id", objMap.get("cw_call_id"));
		
						if("all".equals(type) || "onesection".equals(type)) {
							if(resultGridList.size()>0) {
								cwGrid.add(resultMap);
							}
						}else {
							cwGrid.add(resultMap);	//type이 all 이 아닌 애들
						}
						resultMap = new HashMap<String, Object>();
						
						checkFirstBlock=false;
					}
				} else {
					cwGrid = null;
				}
				//CW status가 0 이었지만, 데이터가 하나도 없었을 경우 처리로직
				if (i == resultList.size()) {
					LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "No Exist Match Data. CW code: 0");
					cwGrid = null;
				}
				param.put("biz_thread_time_end", System.nanoTime() + "");

				return cwGrid;
				
			}
		});		
		
		
		threadPool.execute(task);
		List<Map<String, Object>> resultTask = new ArrayList<Map<String,Object>>();
		try {
			try {
				resultTask = task.get(biztimeout, TimeUnit.MILLISECONDS);
				resultGrid = resultTask;
				
			} catch (TimeoutException e) {
				LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", "cw_biz_timeout");
				param.put("biz_thread_time_end", System.nanoTime() + "");
				param.put("biz_exception", "biz_timeout");
			}
		} catch (InterruptedException e) {
			LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
		} catch (ExecutionException e) {
			LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
		}
				
		
		return resultGrid;
	}
	
	//CW 연관콘텐츠 생성로직
	public List makeCwRelation(Map<String, Object> objMap, String param_epsd_id, Map<String, String> param, Map<String, Object> time) {
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		String type = CastUtil.getObjectToString(objMap.get("type"));
		
		//데이터 추출 메소드
		getCwData(objMap, resultList);
		
//		List<Map<String, Object>>resultRelation = new ArrayList<Map<String,Object>>();
		//데이터 가공로직 시작
		ExecutorService threadPool = Executors.newCachedThreadPool();
		FutureTask<List<Map<String, Object>>> task = new FutureTask<List<Map<String, Object>>>(new Callable<List<Map<String, Object>>>() {
			public List<Map<String, Object>> call() throws Exception {
				List<Map<String, Object>> cwRelation = new ArrayList<Map<String,Object>>();

				int cnt = 0;
				int i = 0;
				boolean checkFirstBlock = true;		
				
				if(resultList!=null && !resultList.isEmpty()) {
					cwRelation = new ArrayList<Map<String,Object>>();
					for(Map<String, Object>temp:resultList ) {
						List<Map<String, Object>>resultRelationList = new ArrayList<Map<String,Object>>();
						Map<String, Object> resultMap = new HashMap<String, Object>();
						List<String> tempIdList = CastUtil.getObjectToListString(temp.get("idList"));
		//				List<String>tempIdList = (List<String>) temp.get("idList");
						if(tempIdList != null && !tempIdList.isEmpty()) {
							List<Map<String, Object>> makedList = new ArrayList<Map<String, Object>>();
							
							for(String dataGrp:tempIdList) {
								String [] idNblockId = dataGrp.split("\\|");
								
								Map<String, Object> relationData = new HashMap<String, Object>();
								
								makedList = makeCWService.makeCwGridMap(idNblockId, relationData, param);
								if (makedList.size() > 0) {
									resultRelationList.add(makedList.get(0));
								}
								cnt++;
							}
//							for (Map<String, Object> makedMap : makedList) {
//								resultRelationList.add(makedMap);
//						    }
//							time.put("redis_count", cnt);
							
						}else {
							//아이템이 없을 경우 카운트 추가
							if(!"section".equals(type)) {
								i++;
							}
						}
						String sub_title=CastUtil.getObjectToString(temp.get("blockTitle"));
						String content_title=CastUtil.getObjectToString(objMap.get("contentTitle"));
						if(sub_title != null && !sub_title.isEmpty()) {
							sub_title = restMatching(param_epsd_id, sub_title, content_title, param);
						}
						
						if(!sub_title.isEmpty()) {
							
							resultMap.put("sectionId", temp.get("sectionId"));
							resultMap.put("session_id", objMap.get("sessionId"));
							if(temp.containsKey("trackId") && temp.get("trackId")!=null && !"".equals(temp.get("trackId"))) {
								resultMap.put("btrack_id", temp.get("trackId"));
							}else {
								resultMap.put("btrack_id", temp.get("btrackId"));
							}
							resultMap.put("block", resultRelationList);
							resultMap.put("t_cnt", resultRelationList.size()+"");
							resultMap.put("sub_title", sub_title);
							resultMap.put("cw_call_id", objMap.get("cw_call_id"));
							
							//onepage호출시 첫번쨰 블록에 데이터가 없으면 리턴
							if("onepage".equals(type) && checkFirstBlock && resultRelationList.size()<=0) {
								LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "NCMS", "No Exist Match Data. CW code: 0");
								checkFirstBlock=false;
								return null;
							}
							
							if("all".equals(type) || "onesection".equals(type)) {
								if(resultRelationList.size()>0) {
									cwRelation.add(resultMap);
								}
							}else {
								cwRelation.add(resultMap);	//type이 all 이 아닌 애들
							}
							resultMap = new HashMap<String, Object>();
							checkFirstBlock=false;
							
						}
					}
				
				}
				
				//CW status가 0 이었지만, 데이터가 하나도 없었을 경우 처리로직
				if(i==resultList.size()) {
					LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("cw_stb_id"), "CW", "No Exist Match Data. CW code: 0");
					cwRelation = null;
				}
				
				return cwRelation;
			}
		});		
	
		threadPool.execute(task);
		List<Map<String, Object>> resultTask = new ArrayList<Map<String,Object>>();
		try {
			try {
				resultTask = task.get(biztimeout, TimeUnit.MILLISECONDS);
				
//				resultRelation = resultTask;
//				LogUtil.tlog(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", resultRelation);
				
			} catch (TimeoutException e) {
				LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", "cw_biz_timeout");
				param.put("biz_thread_time_end", System.nanoTime() + "");
				param.put("biz_exception", "biz_timeout");
			}
		} catch (InterruptedException e) {
			LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
		} catch (ExecutionException e) {
			LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
		}
			
		return resultTask;
	}
	
	//CW API에서 데이터 추출 (con_id등)
	public void getCwData(Map<String, Object> objMap, List<Map<String, Object>> resultList) {
		
		List<Map<String, Object>> sections = CastUtil.getObjectToMapList(objMap.get("sections"));
		
		//단일섹션은 응답값이 다르므로 담는 변수를 따로 둔다.
		List<Map<String, Object>> oneSecBlocks = CastUtil.getObjectToMapList(objMap.get("blocks"));
		
		String type = CastUtil.getObjectToString(objMap.get("type"));

		
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		List<String> idList = new ArrayList<String>();
		
		
		if((sections != null && !sections.isEmpty()) || (oneSecBlocks != null && !oneSecBlocks.isEmpty())) {
			
			switch(type) {
			
			case "all": case "onepage":
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
					result.put("sectionId", sectionMap.get("sectionId"));
					result.put("trackId", objMap.get("trackId"));
	
					if( !( "all".equals(type) && result.get("idList") == null ) ) {
						resultList.add(result);
					}
					result = new HashMap<String, Object>();
				}
				break;
				
			case "multi": case "section":
				for(Map<String, Object>sectionMap:sections) {
					result.put("sectionId", sectionMap.get("sectionId"));
					result.put("trackId", objMap.get("trackId"));
					makeItem(sectionMap, result, idList);
					idList = new ArrayList<String>();
					resultList.add(result);
					result = new HashMap<String, Object>();
				}
				break;
				
			case "onesection":
				for(Map<String, Object>temp:oneSecBlocks) {
					result.put("trackId", objMap.get("trackId"));
					makeItem(temp, result, idList);
					idList = new ArrayList<String>();
					resultList.add(result);
					result = new HashMap<String, Object>();
				}
				break;
				
			default :
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
					result.put("sectionId", sectionMap.get("sectionId"));
					result.put("trackId", objMap.get("trackId"));
					if(type == null) type="all";
					if( !("all".equals(type) && result.get("idList") == null ) ) {
						resultList.add(result);
					}
					result = new HashMap<String, Object>();
				}
				break;
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
				if(itemIds!=null) {
					String contentId = CastUtil.getObjectToString(itemIds.get("CW"));
					String trackId = CastUtil.getObjectToString(itemMap.get("trackId"));
					idList.add(contentId+"|"+trackId);
				}
			}
		}
		result.put("blockTitle",temp.get("blockLabel"));
		if(temp.containsKey("blockId") && temp.get("blockId")!=null) {
			result.put("sectionId", temp.get("blockId"));
		}
		result.put("sessionId", temp.get("sessionId"));
		result.put("btrackId", temp.get("trackId"));
		if(idList != null && idList.size()>0) {
			result.put("idList",idList);
		}else {
			result.put("idList",null);
		}
	}	
	
	
	
	//연관콘텐츠 제목 생성로직
	public String restMatching(String epsd_id, String sub_title, String content_title, Map<String, String> param) {
		
		String actorString = CastUtil.getObjectToString(redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, epsd_id, param));
		
		String genreList = param.get("genreList");
		
		Map<String,List<Object>> codePeopleMap = null;
		if(actorString != null && !actorString.isEmpty()) {
			codePeopleMap = new HashMap<String, List<Object>>();
			codePeopleMap.put("Director", new ArrayList<Object>() );
			codePeopleMap.put("Actor", new ArrayList<Object>() );
			
			List<String> code = new ArrayList<String>();
			List<String> person = new ArrayList<String>();
			String regex="(prs_role_cd)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
			Pattern ptn = Pattern.compile(regex); 
			if (ptn == null) return null;
			
			Matcher matcher = ptn.matcher(actorString); 
			if (matcher == null) return null;
			
			while(matcher.find()){
				code.add(matcher.group(2));
			}
			
			String regexNm="(prs_nm)\"[\\s]*:[\\s]*\\[*\"([^\"]+)\"\\]*";
			Pattern ptnNm = Pattern.compile(regexNm); 
			if (ptnNm == null) return null;
			
			Matcher matcherNm = ptnNm.matcher(actorString); 
			if (matcherNm == null) return null;
			
			while(matcherNm.find()){
				person.add(matcherNm.group(2));
			}
			
			
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
		return repalceData( sub_title, codePeopleMap, content_title, genreList);
		
	}
	
	private String repalceData( String man, Map<String,List<Object>> codePeopleMap, String content_title, String genreList ) {
		
		if(man != null && !man.isEmpty()) {
			man = man.replaceAll("\\#", ""); // # 없애버리기 
			if( man.startsWith("Actor") ) {  // Actor로 시작하는거
				return getReturnData( man, "Actor", codePeopleMap);
				
			}else if( man.startsWith("Director") ) { // Director로 시작하는거
				return getReturnData( man, "Director", codePeopleMap);
			}else if( man.startsWith("Collection") ) {// Collection으로 시작하는거
				return man.replaceAll("Collection", content_title);
			}else if( man.startsWith("GenreSimilarArray") ){
				return man.replaceAll("GenreSimilarArray", genreList);
			}else {
				return man;
			}
		}
		return "";
	}
	
	public String getReturnData(String man, String type, Map<String,List<Object>> codePeopleMap  ) {
	
		if(codePeopleMap!=null && !codePeopleMap.isEmpty()) {
			String pos = StringUtils.defaultIfEmpty( man.split(" ")[0].replace(type, ""), "" ); // 결과값은 숫자(Actor1 의 1)
			if (pos==null) pos = "";
			int cnt = CastUtil.getStringToInteger(pos.equals("") ? "1" : pos);
			
			if( codePeopleMap.get(type).size() < cnt ) {
				return "";
			}else {
				return man.replace(type+pos, codePeopleMap.get(type).get(cnt-1).toString() );
			}
		}else {
			return "";
		}
	}
	
	/*
	private int makeCwGridMap(String[] idNblockId, Map<String, Object> gridData, List<Map<String, Object>> resultGridList, Map<String, String> param) {
		int redisCnt = 0;
		String epsd_rslu_id = idNblockId[0];
		String trackId = idNblockId[1];
		
		Map<String, Object> cidInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO, epsd_rslu_id, param));
		redisCnt++;
		if(cidInfo != null) {
			String sris_id = CastUtil.getObjectToString(cidInfo.get("sris_id"));
			String epsd_id = CastUtil.getObjectToString(cidInfo.get("epsd_id"));
			Map<String, Object> gridInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_CONTENTS_ITEM, epsd_id, param));
			redisCnt++;
			if(gridInfo != null && !gridInfo.isEmpty()) {
			
				gridService.checkBadge(gridInfo);

				gridData.putAll(gridInfo);
				gridData.put("track_id", trackId);
				resultGridList.add(gridData);
			} else {
				
				Map<String, Object> sris = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, sris_id, param));
				redisCnt++;
				Map<String, Object> purchares = null;
				Map<String, Object> epsd = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id, param));
				redisCnt++;
				if(sris != null && epsd != null) {
					Map<String, Object> cwGridMap = CastUtil.getObjectToMap(properties.getCw().get("grid"));
					if (cwGridMap != null) {
						gridData.putAll(cwGridMap);
					}
				
					gridData.put("poster_filename_h", epsd.get("epsd_poster_filename_h"));
					gridData.put("sris_id", sris.get("sris_id"));
					gridData.put("poster_filename_v", epsd.get("epsd_poster_filename_v"));
					gridData.put("epsd_id", epsd.get("epsd_id"));
					gridData.put("wat_lvl_cd", epsd.get("wat_lvl_cd"));
					gridData.put("adlt_lvl_cd", epsd.get("adlt_lvl_cd"));
					gridData.put("title", sris.get("title"));				
					gridData.put("trackId", trackId);	
					gridData.put("synon_typ_cd", (sris.get("sris_typ_cd").equals("01") ? "02" : "01"));
					gridData.put("meta_typ_cd", sris.get("meta_typ_cd"));
					gridData.put("rslu_typ_cd", epsd.get("rslu_typ_cd"));
					gridData.put("kids_yn", sris.get("kids_yn"));
					gridData.put("cacbro_yn", epsd.get("cacbro_yn"));
					resultGridList.add(gridData);
					
					purchares = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PURCHARES, sris_id, param));
					redisCnt++;
				} else {
//					LogUtil.info("", "", "", "", "CW", "CONTENTS NULL : " + epsd_id);
					return redisCnt;
				}
				if (purchares != null) {
					List<Map<String, Object>> listPurchares = CastUtil.getObjectToMapList(purchares.get("products"));
					for (Map<String, Object> p : listPurchares) {
						if (p.containsKey("epsd_id") && p.get("epsd_id").equals(epsd_id)) {
							gridData.put("sale_prc", p.get("sale_prc"));
							break;
						}
					}
				}

				gridService.checkBadge(gridData);
			}
		}
		return redisCnt;
	}
*/
//	private void printProcessTime(Map<String, Object> time, Map<String, String> param) {
//		
//		long cw_start = CastUtil.getObjectToLong(time.get("cw_time_start"));
//		long cw_end = CastUtil.getObjectToLong(time.get("cw_time_end"));
//		
//		long after_start = CastUtil.getObjectToLong(time.get("after_start"));
//		long after_end = CastUtil.getObjectToLong(time.get("after_end"));
//		
//		String redis_count = time.get("redis_count") + "";
//		
//		long cw = (cw_end - cw_start) / 1000000;
//		long after = (after_end - after_start) / 1000000;
//		
//		String log = "";
//		log = "cwredis sum:" + (cw + after) + ",cw:" + cw + ",biz_redis:" + after + ",data_cnt:" + redis_count + ",cwcallid:" + param.get("cw_call_id");
//		
//		LogUtil.info(param.get("IF"), "", param.get("UUID"), param.get("stb_id"), "", log);
//	}
}
