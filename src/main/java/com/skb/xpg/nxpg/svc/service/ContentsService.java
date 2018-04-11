package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@Service
public class ContentsService {

	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private Properties properties;
	
//	@SuppressWarnings("serial")
//	public Map<String, Object> getSynopsis(String ver, Map<String, String> param) {
//
//		Map<String, Object> rtn = properties.getResults();
//		
//		//List<Object> list = redisClient.oget("Contents:{FEF4B660-65F1-11E2-B2A1-FF7E72903272}|Contents:{C0011BCB-6C24-11E7-A687-D9AF8E5F116C}");
//		
//		String json = CastUtil.getObjectToString(redisClient.luaget(new ArrayList<String>(){{ add("Contents"); add("Contents"); }}, "{092561E3-049F-11DF-ACB7-9B52BCC57005}"));
//		
//		rtn.put("content1", CastUtil.StringToJsonMap(json));
//		rtn.put("content2", "");
//		
//		return rtn;
//	}
//	
//	public Map<String, Object> getSynopsis2(String ver, Map<String, String> param) {
//
//		Map<String, Object> rtn = properties.getResults();
//		
//		rtn.put("content1", redisClient.hget("Contents", "{C0011BCB-6C24-11E7-A687-D9AF8E5F116C}"));
//		rtn.put("content2", redisClient.hget("Contents", "{FEF4B660-65F1-11E2-B2A1-FF7E72903272}"));
//		
//		return rtn;
//	}
//	
//	private void DoScript(String collection, Map<String, Object> map) {
//		Map<String, List<String>> scriptMap = properties.getScripts();
//		if (!scriptMap.containsKey(collection)) return;
//		
//		try {
//
//			ScriptEngineManager manager = new ScriptEngineManager();
//
//		    ScriptEngine engine = manager.getEngineByName("JavaScript");
//		    
//		    List<String> list = scriptMap.get(collection);
//		    
//		    for (String val : list) {
//		    	String[] arr = val.split("\\|");
//			    if (arr != null && arr.length > 2) {
//				    engine.put(arr[0], "");
//			    	for (String key : arr[1].split("\\^")) {
//					    engine.put(key, map.get(key) + "");
//			    	}
//				    
//				    engine.eval(arr[2]);
//				    map.put(arr[0], engine.get(arr[0]));
//			    }
//		    }
//		    
//		} catch (ScriptException | NullPointerException e) {[
//			e.printStackTrace();
//		}
//		
//	}
	
//	public Object getContentsRating(String ver, Map<String, String> param) {
//		Map<String, Object> rtn = properties.getResults();
//
//	}
	
	//IF-NXPG-007
	public Map<String, Object> getSynopsisContents(String ver, Map<String, String> param) {
		try {
			Map<String, Object> sris = CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, param.get("sris_id")));
			Map<String, Object> epsd = CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, param.get("epsd_id")));
			
			if(epsd != null) {
				sris.putAll(epsd);
			}
			
			return sris;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//IF-NXPG-010
	public Map<String, Object> getContentsCommerce(String ver, Map<String, String> param) {
		try {
			Map<String, Object> commerce = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_commerce", param.get("sris_id")));
			
			commerce.put("info_id", CastUtil.StringToJsonMap((String) redisClient.hget("contents_phrase", commerce.get("info_id").toString())));
			commerce.put("delivery_info_id", CastUtil.StringToJsonMap((String) redisClient.hget("contents_phrase", commerce.get("delivery_info_id").toString())));
			commerce.put("refund_info_id", CastUtil.StringToJsonMap((String) redisClient.hget("contents_phrase", commerce.get("refund_info_id").toString())));
			return commerce;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//IF-NXPG-011 콘텐츠용 인물정보 불러오기 - 아래 메소드와 같이 수정 필요
	public void getContentsPeople(Map<String, Object> contents, Map<String, String> param) {
		try {
			String redisData = (String) redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, param.get("epsd_id"));
			if(redisData!=null&&!"".equals(redisData)) {
				contents.putAll(CastUtil.StringToJsonMap(redisData));
			}else {
				contents.put("peoples", null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//IF-NXPG-011
	public Map<String, Object> getContentsPeople(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, param.get("epsd_id")));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//IF-NXPG-012
	public Map<String, Object> getContentsPreview(Map<String, Object> contents, Map<String, String> param) {
		try {
			String redisData = (String) redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, param.get("sris_id"));
			if(redisData!=null&&!"".equals(redisData)) {
				contents.putAll(CastUtil.StringToJsonMap(redisData));
			}else {
				contents.put("peoples", null);
			}
			
//			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PREVIEW, param.get("sris_id")));
			return contents;
		} catch (Exception e) {
			return null;
		}
	}	
	
	//IF-NXPG-025
	public Object getContentsSeries(String ver, Map<String, String> param) {
		try {
			Map<String, Object> temp = CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_sris", param.get("sris_id")));
			return temp.get("episodes");
		} catch (Exception e) {
			return null;
		}
	}	
	
	//IF-NXPG-013
	public void getContentsCorner(Map<String, Object> contents, Map<String, String> param) {
		try {
			String redisData = (String) redisClient.hget("contents_corner", param.get("epsd_id"));
			Map <String, Object> redisMap = null;
			if(redisData!=null&&!"".equals(redisData)) {
				redisMap = CastUtil.StringToJsonMap(redisData);
				contents.putAll(redisMap);
			}else {
				contents.put("corners", null);
			}
			
//			return CastUtil.StringToJsonMap((String) redisClient.hget("contents_corner", param.get("epsd_id")));
		} catch (Exception e) {
		}
	}	
	
	//IF-NXPG-013
	public Map<String, Object> getCornerGather(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("corner_gather", param.get("cnr_grp_id")));
		} catch (Exception e) {
			return null;
		}
	}	
	
	//IF-NXPG-014
	public List getContentsGwsynop(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonList((String) redisClient.hget("synopsis_gateway", param.get("sris_id")));
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	//IF-NXPG-015
	public Map<String, Object> getContentsPurchares(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PURCHARES, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}
	
	//IF-NXPG-017
	public Map<String, Object> getContentsVodList(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_VODLIST, param.get("prs_id")));
		} catch (Exception e) {
			return null;
		}
	}

	//IF-NXPG-01?
	public Map<String, Object> getContentsReview(String ver, Map<String, String> param) {
		
		try {
			String redisData = (String) redisClient.hget("contents_review", param.get("sris_id"));
			Map<String, Object> root = null;
			if (redisData != null && !"".equals(redisData)) {
				root = CastUtil.StringToJsonMap(redisData);
				
				int idx = 1;
				int start = CastUtil.getStringToInteger(param.get("page_no"));
				if (start < 1) {
					start = 1;
				}
				int count = CastUtil.getStringToInteger(param.get("page_cnt"));
				// Map<String, Object> menus = CastUtil.getObjectToMap(root.get("menus"));
				List<Map<String, Object>> sites = CastUtil.getObjectToMapList(root.get("sites"));
				Map<String, Object> useSite = null;
				
				for (Map<String, Object> site : sites) {
					if (site.get("site_cd").equals(param.get("site_cd"))) {
						List<Map<String, Object>> reviews = CastUtil.getObjectToMapList(site.get("reviews"));
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						if (reviews != null && reviews.size() > 0) {
							for (Map<String, Object> review : reviews) {
								
								if (review != null && start <= idx && idx <= (start + count - 1)) {
									list.add(review);
								}
								idx++;
							}
						}
						site.put("reviews", list);
						useSite = site;
					}
					
					
				}
				root.put("sites", useSite);
			}
			return root;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//IF-NXPG-01?
	public void getContentsReview(Map<String, Object> contents, Map<String, String> param) {
		try {
			String redisData = (String) redisClient.hget("contents_review", param.get("sris_id"));
			Map<String, Object> root = null;
			if (redisData != null && !"".equals(redisData)) {
				root = CastUtil.StringToJsonMap(redisData);

				Map<String, Object> temp = null;
				// Map<String, Object> menus = CastUtil.getObjectToMap(root.get("menus"));
				List<Map<String, Object>> sites = CastUtil.getObjectToMapList(root.get("sites"));

				for (Map<String, Object> site : sites) {
					temp = null;
					List<Map<String, Object>> reviews = CastUtil.getObjectToMapList(site.get("reviews"));
					if (reviews != null && reviews.size() > 0) {
						temp = reviews.get(0);
					}
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					if (temp != null) {
						list.add(temp);
					}
					site.put("reviews", list);
				}

				contents.put("site_review", root);
			} else {
				contents.put("site_review", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//IF-NXPG-01?
	public Map<String, Object> getPeopleInfo(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("people_info", param.get("prs_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
