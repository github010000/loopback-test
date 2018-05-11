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
import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.rest.RestClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

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
	public void getMenuGnb(String ver, Map<String, String> param, Map<String, Object> rtn) throws Exception {
		String version = StringUtils.defaultIfEmpty(redisClient.hget(NXPGCommon.VERSION, NXPGCommon.MENU_GNB + "_" + param.get("menu_stb_svc_id")), "");
		
		if (version != null && param.containsKey("version") && !version.isEmpty()
				&& CastUtil.getStringToLong(param.get("version")) >= CastUtil.getStringToLong(version)) {
			rtn.put("reason", "최신버전");
			rtn.put("result", "0000");
			rtn.put("version", version);
		} else {
			String redisData = redisClient.hget(NXPGCommon.MENU_GNB, param.get("menu_stb_svc_id"));
			if ( redisData != null && !"".equals(redisData)) {
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
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		}
	}
	
	// IF-NXPG-002
	public void getMenuAll(String ver, Map<String, String> param, Map<String, Object> rtn) throws Exception {
		String version = StringUtils.defaultIfEmpty(redisClient.hget(NXPGCommon.VERSION,NXPGCommon.MENU_ALL + "_" + param.get("menu_stb_svc_id")), "");
		
		if (version != null && param.containsKey("version") && !version.isEmpty()
				&& CastUtil.getStringToLong(param.get("version")) >= CastUtil.getStringToLong(version)) {
			rtn.put("reason", "최신버전");
			rtn.put("result", "0000");
			rtn.put("version", version);
		} else {
			String redisData = redisClient.hget(NXPGCommon.MENU_ALL, param.get("menu_stb_svc_id"));
			if(redisData != null && !"".equals(redisData)) {
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
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		}
	}
	
	// IF-NXPG-003
	public Map<String, Object> getBlockBigBanner(String ver, Map<String, String> param) throws Exception {
//		System.out.println("11111 ::: " + param.get("menu_stb_svc_id") +  "_" + param.get("menu_id"));
		Map<String, Object> bigbanner = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.BIG_BANNER, param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
		
		if (bigbanner != null && bigbanner.get("banners") != null) {
			List<Map<String, Object>> banners = CastUtil.getObjectToMapList(bigbanner.get("banners"));
			DateUtil.getCompare(banners, "dist_fr_dt", "dist_to_dt", false);
			doSegment(banners, param.get("bnr_seg_id"), "cmpgn_id");
			bigbanner.put("banner_count", bigbanner.get("total_count"));
			bigbanner.remove("total_count");
		}
		
		return bigbanner;
	}
	
	// IF-NXPG-003
	public Map<String, Object> getBlockBlock(String ver, Map<String, String> param) throws Exception {
		Map<String, Object> blockblock = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.BLOCK_BLOCK, param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
		
		if (blockblock != null && blockblock.get("blocks") != null) {
			List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
			List<Map<String, Object>>cwResult = new ArrayList<Map<String,Object>>();
			DateUtil.getCompare(blocks, "dist_fr_dt", "dist_to_dt", false);
			doSegment(blocks, param.get("seg_id"), "cmpgn_id");
			List<Map<String, Object>>deleteList = new ArrayList<Map<String,Object>>();
			Map<String, Object> cw = properties.getCw();
			
			boolean firstCW = false;
			int j = 0; //502가 편성된 위치 찾기 용
			int k = 0; //cw추가된 메뉴 계산용
			
			Map<Integer,List<Map<String, Object>>> whichMap = new HashMap<Integer, List<Map<String, Object>>>();
			
			for (Iterator<Map<String,Object>> iterator = blocks.iterator(); iterator.hasNext() ; ) {
				cwResult = new ArrayList<Map<String,Object>>();
				Map<String, Object> map = CastUtil.getObjectToMap(iterator.next());

				//CW menu로 대체한다.
				if(cw.get("scnmthdcd").equals(map.get("scn_mthd_cd"))) {
					
					
					iterator.remove(); //CW를 호출하는 맵파일은 삭제한다.
					k++;
					
					if(!firstCW) {
						List<String> menuData = cwCall(param);
						Map<String, Object> keyAndValue;
						if (menuData != null) {
							for(String temp:menuData) {
								keyAndValue = CastUtil.getObjectToMap(cw.get("block"));
								Map<String, Object> cwRtn = new HashMap<String, Object>();
								cwRtn.putAll(keyAndValue);
								
								temp=temp.replaceAll("\\|", "\\@");
								
								String [] menuNtitle = temp.split("\\@");
								
								//그리드 콘텐츠에 존재하면서 만료일이 넘지 않은 경우 메뉴를 노출시킨다. 이외에는 노출시키지 않음
								String cwPerGridStr = (String)redisClient.hget(NXPGCommon.GRID_CONTENTS, menuNtitle[0]);
								
								if(cwPerGridStr != null && !cwPerGridStr.isEmpty()) {
									List<Map<String,Object>> cwPerGrid = null;
									Map<String ,Object> cwPerMap = null;
									cwPerMap = CastUtil.StringToJsonMap(cwPerGridStr);
									cwPerGrid = CastUtil.getObjectToMapList(cwPerMap.get("contents"));
									List<Map<String, Object>> data = (List<Map<String, Object>>) CastUtil.getObjectToMapList(cwPerGrid);
									DateUtil.getCompare(data, "dist_fr_dt", "dist_to_dt", true);
									if(data != null && !data.isEmpty()) {
										cwRtn.put("menu_id", menuNtitle[0]);
										cwRtn.put("menu_nm", menuNtitle[1]);
										cwRtn.put("dist_to_dt", map.get("dist_to_dt"));
										cwRtn.put("gnb_typ_cd", map.get("gnb_typ_cd"));
										cwRtn.put("dist_fr_dt", map.get("dist_fr_dt"));
										cwRtn.put("menu_nm_exps_yn", map.get("menu_nm_exps_yn"));
										cwRtn.put("scn_mthd_cd", "10");
										cwRtn.put("menus", null);
										
										cwResult.add(cwRtn);
									}
									
									whichMap.put(j, cwResult);
								}
							}
						}
						firstCW = true;
					}
				}
				j++;	//502 위치 찾기 용
				
				map.put("menus", null);
				
				if (("20".equals(map.get("blk_typ_cd")))
						|| ("70".equals(map.get("blk_typ_cd"))) && "Y".equals(map.get("is_leaf"))) {

					Map<String, Object> gridbanner = getGridBanner(param.get("menu_stb_svc_id") + "_" + map.get("menu_id").toString());
					if (gridbanner != null) {
						DateUtil.getCompare(CastUtil.getObjectToMapList(gridbanner.get("banners")), "dist_fr_dt", "dist_to_dt", true);
						doSegment(CastUtil.getObjectToMapList(gridbanner.get("banners")), param.get("seg_id"), "cmpgn_id");
						map.put("menus", gridbanner.get("banners"));
					} else {
						deleteList.add(map);
					}
				}
			}
			
			double totalCount = (double)blockblock.get("total_count");
			
			for( int o = 0 ; o<blocks.size(); o++) {
				if( whichMap.containsKey(o)) {
					blocks.addAll(o, whichMap.get(o));
					
					totalCount += whichMap.get(o).size();
				}
			}
			
			//005에서 데이터 없으면 삭제.
			for (Map<String, Object> del : deleteList) {
				if( blocks.contains(del)) {
					blocks.remove(del);
					k++;
				}
			}
			
			
			blockblock.put("block_count", totalCount - k);
			blockblock.remove("total_count");
		}
		
		return blockblock;
	}
	
	// IF-NXPG-003
	public Map<String, Object> getGridBanner(String menu_id) {
		return CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_BANNER, menu_id));
	}
	
	// IF-NXPG-005
	public void getBlockMonth(Map<String, Object> rtn, Map<String, String> param) throws Exception {
		Map<String, Object> bigbanner = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.BIG_BANNER, param.get("menu_stb_svc_id") + "_" + param.get("menu_id")));
		
		Map<String, Object> blockblock = null;
		
		List<Map<String, Object>> newBlocks = new ArrayList<Map<String, Object>>();
		
		List<Map<String, Object>> banners = null;
		if (bigbanner != null) {
			banners = CastUtil.getObjectToMapList(bigbanner.get("banners"));
			DateUtil.getCompare(banners, "dist_fr_dt", "dist_to_dt", false);
		}
//		List<Map<String, Object>> banners = CastUtil.getObjectToMapList(bigbanner.get("banners"));
		
		List<Object> monthList = CastUtil.StringToJsonList(redisClient.hget(NXPGCommon.BLOCK_MONTH, param.get("menu_stb_svc_id")));
		
		List<Map<String, Object>> user_month = new ArrayList<Map<String, Object>>();
		if (monthList != null) {
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
//							blockblock.put("block_count", blockblock.get("total_count"));
//							blockblock.remove("total_count");
					}
				}
			}
			
			for (Map<String, Object> month_item : user_month) {
				blockblock = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.BLOCK_BLOCK, month_item.get("menu_id") + ""));
				if (blockblock != null && !blockblock.isEmpty()) {
					List<Map<String, Object>> blocks = CastUtil.getObjectToMapList(blockblock.get("blocks"));
					DateUtil.getCompare(blocks, "dist_fr_dt", "dist_to_dt", false);
					
					newBlocks.addAll(blocks);
				}
			}
			
			if (user_month.size() > 0) {
				rtn.put("result", "0000");
				rtn.put("banners", null);
				if (bigbanner != null) {
					rtn.putAll(bigbanner);
				}
				rtn.put("blocks", newBlocks);
				rtn.put("block_count", newBlocks.size());
				rtn.put("month", user_month);
			} else {
				rtn.put("result", "9995");
			}
			
		} else {
			rtn.put("result", "9998");
		}
		
		// 성공
		
		// 카운트 넣어주기 
//		if (bigbanner != null) rtn.put("total_count", bigbanner.size());
	}
	
	public List<String> cwCall(Map<String, String> param) {

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
		
		//응답값 확인
		String restregex="\"code\"[\\s]*:[\\s]*([0-9]*)";
		String codeValue = StrUtil.getRegexString(restregex, rest);

		if("0".equals(codeValue)) {
			//시리즈아이디, 에피소드아이디 추출로직
			String regexMenuId="\\\"MenuIdPreferred\\\"[\\s]*:[\\s]*\\[\"(.*?)\"\\]";
			Pattern ptn = Pattern.compile(regexMenuId); 
			Matcher matcher = ptn.matcher(rest); 
			while(matcher.find()){
				menuData = Arrays.asList((matcher.group(1)).split("\\,"));
				break;
			}
		}else {
			menuData = null;
		}
		
		
		
		return menuData;
	}
	
	public void doSegment(List<Map<String, Object>> menuList, String segmentId, String field_campaign) {
		// menu_cd 제거
		List<Map<String, Object>> deleteListInner = new ArrayList<Map<String, Object>>();
		
		if (menuList != null && segmentId != null && !segmentId.isEmpty()) {
			
			// segmentId 10개까지만 가지고 오게 끔 처리
			String[] segmentIdList = segmentId.split(",");
			for(int i = 0; i < segmentIdList.length; i++) {
				if (i == 10) break;
				segmentId += "," + segmentIdList[i];
			}
			if (segmentId.length() > 0) {
				segmentId = segmentId.substring(1);
			}
			
			for (Map<String, Object> menu : menuList) {
				if (menu.containsKey("menu_cd")) {
					menu.remove("menu_cd");
				}
				
				if (menu.containsKey(field_campaign) && menu.get(field_campaign) != null) {
					if (!StrUtil.isEmpty(menu.get(field_campaign) + "") && !segmentId.contains(menu.get(field_campaign) + "")) {
						deleteListInner.add(menu);
					}
				}
			}
			
			for (Map<String, Object> del : deleteListInner) {
				menuList.remove(del);
			}
		}
	}
	
}
