package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import com.skb.xpg.nxpg.svc.util.DateUtil;

@Service
public class MenuService {

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
	
	
	// IF-NXPG-001
	public void getMenuGnb(String ver, Map<String, String> param, Map<String, Object> rtn) {
		try {
			String version = StringUtils.defaultIfEmpty((String) redisClient.hget("version", NXPGCommon.MENU_GNB), "");
			
			if (version != null && param.containsKey("version")
					&& !version.isEmpty() && param.get("version").compareTo(version) >= 0) {
				rtn.put("reason", "최신버전");
				rtn.put("result", "0000");
				rtn.put("version", version);
			} else {
				String redisData = (String) redisClient.hget(NXPGCommon.MENU_GNB, param.get("menu_stb_svc_id"));
				if (!"".equals(redisData) && redisData != null) {
					List<Object> menugnb = CastUtil.StringToJsonList(redisData);
					List<Map<String, Object>> data = CastUtil.getObjectToMapList(menugnb);
					DateUtil.getCompare(data, "dist_fr_dt", "dist_to_dt", true);

					if (data == null) {
						rtn.put("result", "9998");
					} else {
						
						rtn.put("version", version);
						rtn.put("result", "0000");
						rtn.put("menus", data);
						// 카운트 넣어주기
						if (data != null) {
							rtn.put("total_count", data.size());
						}
					}
				} else {
					rtn.put("result", "9998");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// IF-NXPG-002
	public void getMenuAll(String ver, Map<String, String> param, Map<String, Object> rtn) {
		try {

			String version = StringUtils.defaultIfEmpty((String) redisClient.hget("version",NXPGCommon.MENU_ALL), "");
			
			if (version != null && param.containsKey("version")
					&& !version.isEmpty() && param.get("version").compareTo(version) >= 0) {
				rtn.put("reason", "최신버전");
				rtn.put("result", "0000");
				rtn.put("version", version);
			} else {
				String redisData = (String) redisClient.hget(NXPGCommon.MENU_ALL, param.get("menu_stb_svc_id"));
				if(!"".equals(redisData) && redisData != null) {
					List<Object> menuall = CastUtil.StringToJsonList(redisData);
					List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(menuall);
					DateUtil.getCompare(data, "dist_fr_dt", "dist_to_dt", true);
					
					// 조회값 없음
					if (data == null) {
						rtn.put("result", "9998");
					}
					// 성공
					else {
						rtn.put("version", version);
						rtn.put("result", "0000");
						rtn.put("menus", data);
						// 카운트 넣어주기 
						if (data != null) rtn.put("total_count", data.size());
					}
				}else {
					rtn.put("result", "9998");
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getBlockBigBanner(String ver, Map<String, String> param) {
		try {
//			System.out.println("11111 ::: " + param.get("menu_stb_svc_id") +  "_" + param.get("menu_id"));
			Map<String, Object> bigbanner = CastUtil.StringToJsonMap((String) redisClient.hget("big_banner", param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
			
			List<Map<String, Object>> banners = CastUtil.getObjectToMapList(bigbanner.get("banners"));
			DateUtil.getCompare(banners, "dist_fr_dt", "dist_to_dt", false);
			
			bigbanner.put("banner_count", bigbanner.get("total_count"));
			bigbanner.remove("total_count");
			
			return bigbanner;
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-004
	public Map<String, Object> getBlockBlock(String ver, Map<String, String> param) {
		try {
			
			Map<String, Object> blockblock = CastUtil.StringToJsonMap((String) redisClient.hget("block_block", param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
			List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
			DateUtil.getCompare(blocks, "dist_fr_dt", "dist_to_dt", false);
			List<Map<String, Object>>cwResult = new ArrayList<Map<String,Object>>();
			Map<String, Object> cw = properties.getCw();
			
			int i = 0;
			
			for (Iterator<Map<String,Object>> iterator = blocks.iterator(); iterator.hasNext() ; ) {
//			for (Object block : blocks) {
				Map<String, Object> map = CastUtil.getObjectToMap(iterator.next());

				//CW menu로 대체한다.
				if(cw.get("calltypcd").equals(map.get("call_typ_cd"))) {
					
					iterator.remove(); //CW를 호출하는 맵파일은 삭제한다.
					i++; //블럭 카운트를 맞추기 위해 삭제한 개수만큼 증가
					
					List<String> menuData = cwCall(param);
					Map<String, Object> keyAndValue;
					if (menuData != null) {
						for(String temp:menuData) {
							keyAndValue = CastUtil.getObjectToMap(cw.get("block"));
							Map<String, Object> cwRtn = new HashMap<String, Object>();
							cwRtn.putAll(keyAndValue);
							String [] menuNtitle = temp.split("\\|");
							cwRtn.put("menu_id", menuNtitle[0]);
							cwRtn.put("menu_nm", menuNtitle[1]);
							cwRtn.put("dist_to_dt", null);
							cwRtn.put("gnb_typ_cd", null);
							cwRtn.put("dist_fr_dt", null);
							cwRtn.put("menus", null);
							
							cwResult.add(cwRtn);
						}
					}
					
				}
				
				Map<String, Object> gridbanner = getGridBanner(param.get("menu_stb_svc_id") + "_" + map.get("menu_id").toString());
				map.put("menus", null);
				if (gridbanner != null) {
					map.put("menus", gridbanner.get("banners"));
				}
			}
			
			for(Map<String, Object>temp:cwResult) {
				blocks.add(temp);
			}
			
			double totalCount = (double)blockblock.get("total_count");
			
			blockblock.put("block_count", totalCount + cwResult.size()-i);
			blockblock.remove("total_count");
			return blockblock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getGridBanner(String menu_id) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("grid_banner", menu_id));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// IF-NXPG-005
	public void getBlockMonth(Map<String, Object> rtn, Map<String, String> param) {
		try {
			Map<String, Object> bigbanner = CastUtil.StringToJsonMap((String) redisClient.hget("big_banner", param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
			
			Map<String, Object> blockblock = null;
//			Map<String, Object> blockblock = CastUtil.StringToJsonMap((String) redisClient.hget("block_block", param.get("menu_id")));
			
			List<Map<String, Object>> newBlocks = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> banners = CastUtil.getObjectToMapList(bigbanner.get("banners"));
			DateUtil.getCompare(banners, "dist_fr_dt", "dist_to_dt", false);
			
			List<Object> monthList = CastUtil.StringToJsonList((String) redisClient.hget("block_month", param.get("menu_stb_svc_id")));
			
			List<Map<String, Object>> user_month = new ArrayList<Map<String, Object>>();
			for (Object month : monthList) {
				Map<String, Object> tempMonth = CastUtil.getObjectToMap(month);
				if (tempMonth.get("prd_prc_id") != null) {
					String tm = tempMonth.get("prd_prc_id") + "";
					if (param.get("prd_prc_id_lst").contains(tm)) {
						List<Map<String, Object>> lowrank = CastUtil.getObjectToMapList(tempMonth.get("low_rank_products"));
						if (lowrank == null || lowrank.size() < 1) {
							user_month.add(tempMonth);
						} else {
							for (Map<String, Object> low : lowrank) {
								user_month.add(low);
							}
						}
//						blockblock.put("block_count", blockblock.get("total_count"));
//						blockblock.remove("total_count");
					}
				}
			}
			
			for (Map<String, Object> month_item : user_month) {
				blockblock = CastUtil.StringToJsonMap((String) redisClient.hget("block_block", month_item.get("menu_id") + ""));
				if (blockblock != null && !blockblock.isEmpty()) {
					List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
					DateUtil.getCompare(blocks, "dist_fr_dt", "dist_to_dt", false);
					
					newBlocks.addAll(blocks);
				}
			}
			
			
			// 성공
			rtn.put("result", "0000");
			if (bigbanner != null) {
				rtn.putAll(bigbanner);
			}
			if (newBlocks != null) {
				rtn.put("blocks", newBlocks);
				rtn.put("block_count", newBlocks.size());
			}
			rtn.put("month", user_month);
			
			
			// 카운트 넣어주기 
//				if (bigbanner != null) rtn.put("total_count", bigbanner.size());
			
		} catch (Exception e) {
			rtn.put("result", "9999");
			e.printStackTrace();
			
		}
	}
	
	
	public List<String> cwCall(Map<String, String> param){

		List<String> menuData = null;

		Map<String, Object> cw = properties.getCw();
		Map<String, Object> keyAndValue;
		
		String cwparam = "";

		keyAndValue = CastUtil.getObjectToMap(cw.get("userpage"));
					
		String path = keyAndValue.get("uri").toString();
		String regex = "\\{(.*?)\\}";
		path = path.replaceAll(regex, param.get("stb_id"));

		String rest = null;
		rest = restClient.getRestUri(cwBaseUrl + path, cwUser, cwPassword, cwparam);
		
		
		//시리즈아이디, 에피소드아이디 추출로직
		String regexMenuId="\\\"MenuIdPreferred\\\"[\\s]*:[\\s]*\\[\"(.*?)\"\\],";
		Pattern ptn = Pattern.compile(regexMenuId); 
		Matcher matcher = ptn.matcher(rest); 
		while(matcher.find()){
			menuData = Arrays.asList((matcher.group(1)).split("\\,"));
			break;
		}
//		System.out.println(menuData.toString());
		
		return menuData;
	}
	
}
