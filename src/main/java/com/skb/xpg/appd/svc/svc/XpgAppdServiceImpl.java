package com.skb.xpg.appd.svc.svc;

import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.util.*;
import com.skb.xpg.appd.svc.xpg.ResultCommon;
import com.skb.xpg.appd.svc.xpg.XpgCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.*;

@SuppressWarnings("unchecked")
@Service
public class XpgAppdServiceImpl implements XpgAppdService {
	private final static Logger logger = LoggerFactory.getLogger(XpgAppdServiceImpl.class.getName());
	public final static String D_TYPE_OF_YOUTUBE = "65";
	
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private ResponseUtil responseUtil;
	@Autowired
	private ResultCommon resultCommon;
	
	//홈배너 Home Menu Bannel (AG-XP-00021:IF-XPG-APPD-001)
	@Override
	public Map<String, Object> getHomeMenuBanner(Map<String, String> param) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		try{	
			String ui_name		= param.get("ui_name");
			String menu_id		= param.get("menu_id");
			String prom_resltn_cd	= param.get("prom_resltn_cd");

			// 프로모션 롤링메뉴 처리
			Map<String, Object> ui_prom_map = (Map<String, Object>) redisClient.hget(XpgCommon.HOME_BANNER, ui_name);
			
			List<Map<String, Object>> prom_menu_list = null;
			
			if(ui_prom_map !=null && ui_prom_map.size()>0){
				prom_menu_list = (List<Map<String, Object>>) ui_prom_map.get(menu_id);
				
				if(prom_menu_list != null && !prom_menu_list.isEmpty()){
					if(!"".equals(prom_resltn_cd)) {
						String promo_img;
						String [] array = null;
						for (Map<String, Object> p_m : prom_menu_list) {
							List<Map<String,Object>> menus = (List<Map<String, Object>>) p_m.get("menus");
							if(menus != null && menus.size()>0){
								for(Map<String, Object>tmp:menus){
									promo_img = (String) tmp.get("promo_img");
									if(promo_img != null && !"".equals(promo_img)){
										array = promo_img.split("\\.");
										tmp.put("promo_img", array[0] + "_"+ prom_resltn_cd +"." + array[1]);
									}
									
									tmp.remove("service_id");
									tmp.remove("disp_cd");
									tmp.remove("big_img");
									tmp.remove("ch_no");
									tmp.remove("priority");
									tmp.remove("yn_use");
									tmp.remove("st_mid");
									tmp.remove("depth");
									tmp.remove("m_desc");
									tmp.remove("cw_page_item");
								}
							}
							
							p_m.remove("disp_cd");
							p_m.remove("big_img");
							p_m.remove("ch_no");
							p_m.remove("priority");
							p_m.remove("yn_use");
							p_m.remove("st_mid");
							p_m.remove("depth");
							p_m.remove("m_desc");
							p_m.remove("cw_page_item");
						}	
					}
				}
			}
			
			result.put("result", "0000");
			result.put("reason", "");
			result.put("promotion", prom_menu_list);
		}catch(Exception e) {
			if( logger != null){logger.error("Error", e);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR");
		}
		
		return result;
	}
	
	//전체메뉴 배너 (AG-XP-00022:IF-XPG-APPD-002)
	@Override
	public Map<String, Object> getPromoMenu(Map<String, String> param) {
		String ui_name = param.get("ui_name");
		String prom_top_cd = param.get("prom_top_cd");
		String prom_bottom_cd = param.get("prom_bottom_cd");
		
		Map<String, Object> result = null;
		String temp;
		String [] array;
		try {
			result = (Map<String, Object>) redisClient.hget(XpgCommon.ALL_MENU_BANNER, ui_name);
			
			if(result == null) {
				result = new HashMap<String, Object>();
				result.put("result", "9999");
				result.put("reason", "프로모션 메뉴 데이터 없음.");
			} else {
				List<Map<String, Object>> oneDepthMenu = (List<Map<String,Object>>) redisClient.hget(XpgCommon.ONE_DEPTH_BANNER, ui_name);
				if(oneDepthMenu == null || oneDepthMenu.size() == 0) {
					result = new HashMap<String, Object>();
					result.put("result", "9999");
					result.put("reason", "1뎁스 전체메뉴 데이터 없음.");
				} else { //NXTPROMGENREMENU
					Map<String, Object> menuDataMap = (Map<String, Object>) redisClient.hget(XpgCommon.ALL_MENU_CATEGORY_BANNER, ui_name);
					if(menuDataMap == null) {
						result = new HashMap<String, Object>();
						result.put("result", "9999");
						result.put("reason", "카테고리 프로모션 메뉴 데이터 없음.");
					} else {
						List<Map<String, Object>> prom_roll_menus = new ArrayList<Map<String,Object>>();
						List<Map<String, Object>> menuList;
						Map<String, Object> promGenreMenu;
						for (Map<String, Object> o_d_m : oneDepthMenu) {
							promGenreMenu = new HashMap<String, Object>();
							String strMenuId = (String) o_d_m.get("menu_id");
							String strLinkMenuId = (String) o_d_m.get("link_menu_id");
							if(!"".equals(strLinkMenuId)) { // 라이브러리 메뉴 아이디가 있으면 메뉴 아이디 변경
								promGenreMenu.put("menu_id", strLinkMenuId);
							} else {
								promGenreMenu.put("menu_id", strMenuId);
							}
							promGenreMenu.put("m_name", o_d_m.get("menu_nm"));
							menuList = (List<Map<String, Object>>) menuDataMap.get(strMenuId); // 하위 데이터를 가져올땐 라이브러리 메뉴아이디가 있어도 메뉴아이디로 가져옴
							if(menuList != null && menuList.size() > 0) {
								promGenreMenu.put("prom_menu_yn", "Y");
								if(!"".equals(prom_top_cd) && !"".equals(strLinkMenuId)) { // 해상도값과 라이브러리 메뉴아이디값이 있을때
									// 1Depth 상단 프로모션 이미지 해상도 적용
									for (Map<String, Object> m : menuList) {
										m.put("par_menu_id", strLinkMenuId); // 부모 메뉴 아이디를 라이브러리 메뉴아이디로 변경
										temp = (String) m.get("promo_img");
										if(temp != null && !temp.isEmpty()){
											array = temp.split("\\.");
											m.put("promo_img", array[0] + "_"+ prom_top_cd +"." + array[1]);
										}
										
										removeMenusElement(m);
									}
								} else if (!"".equals(prom_top_cd) && "".equals(strLinkMenuId)) { // 해상도값은 있고 라이브러리 메뉴아이디값은 없을때
									// 1Depth 상단 프로모션 이미지 해상도 적용
									for (Map<String, Object> m : menuList) {
										temp = (String) m.get("promo_img");
										if(temp != null && !temp.isEmpty()){
											array = temp.split("\\.");
											m.put("promo_img", array[0] + "_"+ prom_top_cd +"." + array[1]);
										}
										
										removeMenusElement(m);
									}
								} else if ("".equals(prom_top_cd) && !"".equals(strLinkMenuId)) { // 해상도값은 없고 라이브러리 메뉴아이디값은 있을때
									for (Map<String, Object> m : menuList) {
										m.put("par_menu_id", strLinkMenuId); // 부모 메뉴 아이디를 라이브러리 메뉴아이디로 변경
										
										removeMenusElement(m);
									}
								}
								promGenreMenu.put("promotion_top", menuList);
							} else {
								promGenreMenu.put("prom_menu_yn", "N");
								promGenreMenu.put("promotion_top", new ArrayList<Map<String,Object>>());
							}
							prom_roll_menus.add(promGenreMenu);
						}
						result.put("prom_roll_menus", prom_roll_menus);
						result.put("result", "0000");
						result.put("reason", "");
						result.put("ui_name", ui_name);
						result.put("version", "");
					}
				}
				
				// 안쓰는 파라미터 정리 및 이미지 해상도 적용
				Map<String, Object> promTopMenuMap = (Map<String, Object>)result.get("promotion_top");
				removePromElement(promTopMenuMap);
				
				List<Map<String, Object>> promTopMenu = (List<Map<String, Object>>) promTopMenuMap.get("menus");
				if(promTopMenu != null) {
					for (Map<String, Object> m : promTopMenu) {
						removeMenusElement(m);
						
						// 상단 프로모션 이미지 해상도 적용
						if(!"".equals(prom_top_cd)) {
							temp = (String) m.get("promo_img");
							if(temp != null && !temp.isEmpty()){
								array = temp.split("\\.");
								m.put("promo_img", array[0] + "_"+ prom_top_cd +"." + array[1]);
							}
						}
					}
				}

				// 안쓰는 파라미터 정리 및 이미지 해상도 적용
				Map<String, Object> promBottomMenuMap = (Map<String, Object>)result.get("promotion_bottom");
				removePromElement(promBottomMenuMap);
				
				List<Map<String, Object>> promBottomMenu = (List<Map<String, Object>>) promBottomMenuMap.get("menus");
				if(promBottomMenu != null) {
					for (Map<String, Object> m : promBottomMenu) {
						removeMenusElement(m);
						
						// 하단 프로모션 이미지 해상도 적용
						if(!"".equals(prom_bottom_cd)) {
							temp = StringUtils.defaultIfEmpty((String) m.get("promo_img"), "");
							if(temp != null && !temp.isEmpty()){
								array = temp.split("\\.");
								m.put("promo_img", array[0] + "_"+ prom_bottom_cd +"." + array[1]);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if( logger != null){logger.error("Error", e);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR");
		}
		
		return result;
	}
	
	private void removePromElement(Map<String, Object> dataMap){
		if( dataMap != null){
			if (dataMap.containsKey("service_id")) dataMap.remove("service_id");
			if (dataMap.containsKey("big_img")) dataMap.remove("big_img");
			if (dataMap.containsKey("is_leaf")) dataMap.remove("is_leaf");
			if (dataMap.containsKey("on_img")) dataMap.remove("on_img");
			if (dataMap.containsKey("priority")) dataMap.remove("priority");
			if (dataMap.containsKey("yn_use")) dataMap.remove("yn_use");
			if (dataMap.containsKey("yn_img_sts")) dataMap.remove("yn_img_sts");
			if (dataMap.containsKey("off_img")) dataMap.remove("off_img");
			if (dataMap.containsKey("st_mid")) dataMap.remove("st_mid");
			if (dataMap.containsKey("depth")) dataMap.remove("depth");
			if (dataMap.containsKey("m_desc")) dataMap.remove("m_desc");
			if (dataMap.containsKey("cw_page_item")) dataMap.remove("cw_page_item");
			
			if (dataMap.containsKey("ch_no")) dataMap.remove("ch_no");
			if (dataMap.containsKey("yn_adult")) dataMap.remove("yn_adult");
			if (dataMap.containsKey("promo_img")) dataMap.remove("promo_img");
			if (dataMap.containsKey("vass_id")) dataMap.remove("vass_id");
			if (dataMap.containsKey("call_url")) dataMap.remove("call_url");
			if (dataMap.containsKey("call_type")) dataMap.remove("call_type");
			if (dataMap.containsKey("item_id")) dataMap.remove("item_id");
			if (dataMap.containsKey("d_type")) dataMap.remove("d_type");
			if (dataMap.containsKey("s_v_style")) dataMap.remove("s_v_style");
		}
	}
	
	private void removeMenusElement(Map<String, Object> dataMap){
		if( dataMap != null){
			if (dataMap.containsKey("service_id")) dataMap.remove("service_id");
			if (dataMap.containsKey("menus")) dataMap.remove("menus");
			if (dataMap.containsKey("t_cnt")) dataMap.remove("t_cnt");
			if (dataMap.containsKey("big_img")) dataMap.remove("big_img");
			if (dataMap.containsKey("is_leaf")) dataMap.remove("is_leaf");
			if (dataMap.containsKey("on_img")) dataMap.remove("on_img");
			if (dataMap.containsKey("priority")) dataMap.remove("priority");
			if (dataMap.containsKey("yn_use")) dataMap.remove("yn_use");
			if (dataMap.containsKey("yn_img_sts")) dataMap.remove("yn_img_sts");
			if (dataMap.containsKey("off_img")) dataMap.remove("off_img");
			if (dataMap.containsKey("st_mid")) dataMap.remove("st_mid");
			if (dataMap.containsKey("depth")) dataMap.remove("depth");
			if (dataMap.containsKey("m_desc")) dataMap.remove("m_desc");
			if (dataMap.containsKey("cw_page_item")) dataMap.remove("cw_page_item");
		}
	}
	
	// 컨텐츠 별 평점 및 부가정보 (AG-XP-00023:IF-XPG-APPD-003)
	@Override
	public Map<String, Object> getContentPntEpiInfo(Map<String, String> param) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> contentMap = null;
		
		try{	
			String content_id	= param.get("content_id");
			String st_cd		= param.get("st_cd");
			String st_b_cd		= param.get("st_b_cd");
			String hero_cd		= param.get("hero_cd");
			String ui_name		= param.get("ui_name");
			String cmtCd		= param.get("cmt_cd");
			String cmtBigCd		= param.get("cmt_b_cd");
			
			String epi_resltn_cd 		= StringUtils.defaultIfEmpty(param.get("epi_resltn_cd"), "");
			String epi_scr_resltn_cd	= StringUtils.defaultIfEmpty(param.get("epi_scr_resltn_cd"), "");
			
			Map<String, Object> org_vo = (Map<String, Object>) redisClient.hget(XpgCommon.CONTENTS, content_id);
			
			if(org_vo !=null){
				String master_id = (String) org_vo.get("m_id");
				Map<String, Object> metaContentsVo = null;
				List<Map<String, Object>> epiInfoList = null;
				
				if( master_id != null && !"".equals(master_id)) {
					contentMap = new HashMap<String, Object>();
					
					contentMap.put("con_id", content_id);
					
					metaContentsVo = (Map<String, Object>) redisClient.hget(XpgCommon.META_CONTENTS, master_id);
					
					if( metaContentsVo != null){
						contentMap.put("trivia_info", StringUtils.defaultIfEmpty((String) metaContentsVo.get("trivia_info"), ""));
						contentMap.put("release_dt", StringUtils.defaultIfEmpty((String) metaContentsVo.get("release_dt"), ""));
						
						List<Map<String, Object>> stImageList = (List<Map<String, Object>>) metaContentsVo.get("still_img");
						if( stImageList != null && stImageList.size() > 0){
							for( Map<String, Object> stImageMap:stImageList){
								String[] temp = ((String) stImageMap.get("image")).split("\\.");
								stImageMap.put("image", temp[0] + "_" + st_cd + "." + temp[1]);
								stImageMap.put("big_image", temp[0] + "_" + st_b_cd + "." + temp[1]);
							}
						}
						contentMap.put("st_images", stImageList);
						
						String bgImg = StringUtils.defaultIfEmpty((String) metaContentsVo.get("bg_img"), "");
//						if( !"".equals(bgImg) && hero_cd != null && !hero_cd.equals("")){
						if( !bgImg.isEmpty() && hero_cd != null && !hero_cd.isEmpty()){
							String[] temp = bgImg.split("\\.");
							bgImg = temp[0] + "_" + hero_cd + "." + temp[1];
						}
						contentMap.put("bg_img", bgImg);
						
						epiInfoList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.EPI_N_EPI, master_id);
						if( epiInfoList != null && epiInfoList.size() > 0){
							for( Map<String, Object> epiInfoMap:epiInfoList){
								String[] temp = ((String) epiInfoMap.get("epi_img")).split("\\.");
								if (!epi_resltn_cd.isEmpty()) epiInfoMap.put("epi_img", temp[0] + "_" + epi_resltn_cd + "." + temp[1]);
								if (!epi_scr_resltn_cd.isEmpty()) epiInfoMap.put("epi_scr_img", temp[0] + "_" + epi_scr_resltn_cd + "." + temp[1]);	//???????????????
							}
						}
						contentMap.put("epi_info", epiInfoList);
						

						Map<String, Object> pointMap = (Map<String, Object>) redisClient.hget(XpgCommon.BTV_MOV_POINT, master_id);
						if (pointMap != null && pointMap.containsKey("btv_pnt")) {
							contentMap.put("btv_pnt", pointMap.get("btv_pnt"));
						}else{
							contentMap.put("btv_pnt", "");
						}
						
						List<Map<String, Object>> commentaryMovList = new ArrayList<>();
						List<Map<String, Object>> commentaryImageList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.COMMENTARY_IMAGE, master_id);
						if( commentaryImageList != null){
							for( Map<String, Object> commentaryImageMap:commentaryImageList){
								Map<String, Object> commentaryMovMap = new HashMap<>();
								commentaryMovMap.put("con_id", commentaryImageMap.get("con_id"));
								commentaryMovMap.put("cmt_type_cd", commentaryImageMap.get("pcim_addn_typ_cd"));
								commentaryMovMap.put("title", commentaryImageMap.get("title"));
								
								String trailerImg = StringUtils.defaultIfEmpty((String) commentaryImageMap.get("trailer_img"), "");
								String cmtImg = trailerImg;
								String cmtBigImg = trailerImg;
								
								if (trailerImg != null && trailerImg.length()>0 && trailerImg.contains(".")) {
									String[] temp = trailerImg.split("\\.");
									if (!"".equals(cmtCd)) {
										cmtImg = temp[0] + "_" + cmtCd + "." + temp[1];
									}
									
									if (!"".equals(cmtBigCd)) {
										cmtBigImg = temp[0] + "_" + cmtBigCd + "." + temp[1];
									}
								}
								commentaryMovMap.put("cmt_img", cmtImg);
								commentaryMovMap.put("cmt_b_img", cmtBigImg);
								
								commentaryMovMap.put("yn_use", commentaryImageMap.get("yn_use"));
								commentaryMovMap.put("cmt_type_name", commentaryImageMap.get("pcim_addn_typ_nm"));
								
								commentaryMovList.add(commentaryMovMap);
							}
						}
						contentMap.put("commentary_mov", commentaryMovList);
					}else{
						contentMap.put("trivia_info", "");
						contentMap.put("release_dt", "");
						contentMap.put("st_images", null);
						contentMap.put("bg_img", "");
						contentMap.put("epi_info", null);
						contentMap.put("btv_pnt", "");
						contentMap.put("commentary_mov", null);
					}
				}
			}
			
			result.put("result", "0000");
			result.put("reason", resultCommon.resultCodeListMap.get("0000"));
			result.put("content", contentMap);
		}catch(Exception e) {
			if( logger != null){logger.error("Error", e);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", resultCommon.resultCodeListMap.get("9999"));
		}
		
		return result;
	}
	
	// 연관컨텐츠 (AG-XP-00024:IF-XPG-APPD-004)
	@Override
	public Map<String, Object> getContentRefInfo(Map<String, String> param) {
		Map<String, Object> result = null;
		
		try{
			String con_id		= (String)param.get("contentId");
			String rst_type 	= (String)param.get("rst_type");
			String link_resltn_cd 	= (String)param.get("link_resltn_cd");
			
			String m_id 		= (String) ((Map<String, Object>) redisClient.hget(XpgCommon.CONTENTS, con_id)).get("m_id");
			
			// 관련 VOD 목록 가져오기 
			Map<String, Object> refInfoMap = null;
			
			Map<String, Object> content_info = null;
			content_info = (Map<String, Object>) redisClient.hget(XpgCommon.META_INFO, con_id);
			
			List<String> m_content_list = null;
			if( m_id != null && !"".equals(m_id) ) {
				m_content_list = (List<String>) redisClient.hget(XpgCommon.M_VODS_MAP, m_id);

			}
			
			
			if(content_info !=null){
				refInfoMap = new HashMap<String, Object>();
				List<Map<String, Object>> seriesList = new ArrayList<Map<String,Object>>();
				String sub_title	= StringUtils.defaultIfEmpty((String)content_info.get("relation_name"), "관련콘텐츠");
				String title		= StringUtils.defaultIfEmpty((String)content_info.get("svc_title"), "");
		        
		        List<String> recommand_list = StringUtils.getStringToList((String)content_info.get("recommand_content"), ",");
		        List<String> related_list = StringUtils.getStringToList((String)content_info.get("related_content"), ",");
		        
		        //기존 시스템 연동
		        if(recommand_list !=null && recommand_list.size() >0){
//		        	System.out.println("recommand_list size: " + recommand_list.size());
		        	addSeriesList(recommand_list, seriesList, link_resltn_cd, rst_type);
				}
		        
		        if(m_content_list != null && m_content_list.size() > 0) {
//		        	System.out.println("m_content_list size: " + m_content_list.size());
		        	addSeriesList(m_content_list, seriesList, link_resltn_cd, rst_type);
		        }else if(related_list != null && related_list.size() > 0) {
//		        	System.out.println("related_list size: " + related_list.size());
		        	addSeriesList(related_list, seriesList, link_resltn_cd, rst_type);
		        }
				
	        	//30개가 넘어가면 30개까지만 처리
		        if(seriesList.size() > XpgCommon.MAX_LIST_CNT) {
		        	for(int i = seriesList.size()-1 ; i >= XpgCommon.MAX_LIST_CNT ; i--) {
		        		seriesList.remove(i);
		        	}
		        }
		        	        		        	 
		        refInfoMap.put("con_id", con_id);
		        refInfoMap.put("title", title);
		        refInfoMap.put("s_title", sub_title);
		        refInfoMap.put("series", seriesList);
		        refInfoMap.put("t_cnt", (seriesList != null ? String.valueOf(seriesList.size()) : "0"));
		        
		        result = new HashMap<String, Object>();
	        	result.put("result", "0000");
	        	result.put("reason", "");
	        	result.put("ref_info", refInfoMap);
			}			
		}catch(Exception e) {
			if( logger != null){logger.error("Error", e);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR");
		}
		
		if(result == null) {
			result = new HashMap<String, Object>();
			result.put("result", "7163");
			result.put("reason", "실시간 방송 연관 VOD 리스트 생성 오류");
		}
		return result;
	}
	
	public void addSeriesList(List<String> keyList, List<Map<String, Object>> seriesList, String linkResltnCd, String rstType){
    	for(String key : keyList) {
    		Map<String, Object> content = null;
//			content = (Map<String, Object>) redisClient.hget(XpgCommon.META_INFO, key);
			Object tempObj = redisClient.hget(XpgCommon.META_INFO, key);
    		if( tempObj instanceof Map){
    			content = (Map<String, Object>) tempObj;
    		}
			
			if(content != null) {
				String content_id		= StringUtils.defaultIfEmpty((String)content.get("skb_cid"), "");
				String cur_menu			= StringUtils.defaultIfEmpty((String)content.get("cms_menu"), "");
				String nm_poster_file	= StringUtils.defaultIfEmpty((String)content.get("nm_poster_file"), "");
				String content_title	= StringUtils.defaultIfEmpty((String)content.get("svc_title"), "");
				String content_type		= StringUtils.defaultIfEmpty((String)content.get("content_type"), "");
				String grade_internal	= StringUtils.defaultIfEmpty((String)content.get("grade_internal"), "");
				String fg_quality		= StringUtils.defaultIfEmpty((String)content.get("fg_quality"), "");
				String yn_adult			= StringUtils.defaultIfEmpty((String)content.get("yn_adult"), "");	// 신규메타(vodsNew)는 yn_adult로 내림		by oh2jj - 2017.05.22
				 
				HashMap<String, Object> content_vo = new HashMap<String, Object>();
				 
				if("eros".equals(content_type)) {
					yn_adult = "Y";
				}
				
				// 신규메타(vodsNew)는 yn_adult로 내림		by oh2jj - 2017.05.22
				if("Y".equals(yn_adult)) {
					yn_adult = "Y";
				}
				 
				content_vo.put("con_id", content_id);
				content_vo.put("c_menu", cur_menu);
				content_vo.put("title", content_title);
				if(!"".equals(linkResltnCd)){
					String temp1 = "";
					if( nm_poster_file != null){temp1 = nm_poster_file.replace(".", "_"+ linkResltnCd +".");}
					content_vo.put("poster", temp1);
				} else {
					content_vo.put("poster", nm_poster_file);	
				}
				content_vo.put("yn_adult", yn_adult);
				content_vo.put("level", grade_internal);
				 
				if(content_id != null) {
					
					if("HD".equalsIgnoreCase(rstType) && "30".equals(fg_quality)){//rst_type이 hd이면서 VODS안의 fg_quality값이 30(UHD)면 스킵
						if( logger != null)logger.info("rst_type value is ::::: "+rstType+", but VOD fg_quality is :::::"+fg_quality+"... adding content_list skip");
					} else if ("SD".equalsIgnoreCase(rstType) && ("30".equals(fg_quality) || "20".equals(fg_quality))) {
						if( logger != null)logger.info("rst_type value is ::::: "+rstType+", but VOD fg_quality is :::::"+fg_quality+"... adding content_list skip");
					} else{
						seriesList.add(content_vo);
					}
				}
			}					 
		}
    }
	
	// 인물정보 (AG-XP-00025:IF-XPG-APPD-005)
	@Override
	public Map<String, Object> getStaffInfoList(Map<String, String> param) {
		Map<String, Object> result = null;
		
		try{
			String id			= param.get("id");
			String id_package	= param.get("packageId");
			String resltn_cd	= param.get("resltn_cd");
			
			Map<String, Object> vo = null;
			Map<String, Object> personRefMap = (Map<String, Object>) redisClient.hget(XpgCommon.META_PRSN_INFO, id);
			
			if(personRefMap != null) {
				//스포츠 그룹인지 인물인지 판단
				String gender = (String)personRefMap.get("gender");
				if("100".equals(gender)) {
					//스포츠 그룹 정보 가져오기
					vo = responseUtil.getXpgSportGroupRefVO(id, id_package);
				}else {
					//인물정보에 맞는 컨텐츠 가져오기
					vo = responseUtil.getXpgPersonRefVO(id, id_package);
				}	
		        
		        result = makeStaffRef(vo, id_package, resltn_cd);
		        if(result != null) {
		        	result.put("result", "0000");
		        	result.put("reason", "");
		        }
			}
		}catch(Exception e) {
			if( logger != null){logger.error("Error", e);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR");
		}
		
		if(result == null) {
			result = new HashMap<String, Object>();
			result.put("result", "7157");
			result.put("reason", "인물관련 데이터 생성 오류");
		}
		return result;
	}
	
	// STAFF XML 데이타를 만든다.
	public Map<String, Object> makeStaffRef(Map<String, Object> vo, String id_package, String resltn_cd) throws Exception {
		Map<String, Object> result = null;

		try {
			result = new HashMap<String, Object>();

			String staff_id = (String) vo.get("staff_id");
			String poster = (String) vo.get("poster_filename");
			String title = (String) vo.get("title");
			String icon_image = (String) vo.get("icon_image");

			Map<String, Object> staff = new HashMap<String, Object>();
			staff.put("staff_id", staff_id);
			staff.put("poster", poster);
			staff.put("title", title);
			staff.put("i_img", icon_image);

			List<Map<String, Object>> seriesList = (List<Map<String, Object>>) vo.get("series");
			ArrayList<Map<String, Object>> series = null;
			if (seriesList != null && seriesList.size() > 0) {
				series = new ArrayList<Map<String, Object>>();

				Map<String, Object> sContent = null;
				for (Map<String, Object> evo : seriesList) {
					sContent = new HashMap<String, Object>();

					sContent.put("con_id", evo.get("content_id"));
					sContent.put("title", StringUtils.defaultIfEmpty((String) evo.get("title"), ""));
					sContent.put("level", StringUtils.defaultIfEmpty((String) evo.get("domestic_level"), ""));
					sContent.put("c_menu", StringUtils.defaultIfEmpty((String) evo.get("cur_menu"), ""));
					
					// 포스터 이미지 사이즈 변경
					if (!"".equals(resltn_cd)) {
						String temp1 = (String) evo.get("poster_filename");
						if (temp1 != null) {
							String[] array1 = temp1.split("\\.");
							sContent.put("poster", array1[0] + "_" + resltn_cd + "." + array1[1]);
						}
					} else {
						sContent.put("poster", StringUtils.defaultIfEmpty((String) evo.get("poster_filename"), ""));
					}
					
					sContent.put("yn_adult", StringUtils.defaultIfEmpty((String) evo.get("yn_adult"), ""));

					series.add(sContent);
				}
			}
			staff.put("series", series);

			List<Map<String, Object>> cInfoList = null;
			List<Map<String, Object>> cList = (List<Map<String, Object>>) vo.get("content_info_list");
			if (cList != null && cList.size() > 0) {
				cInfoList = new ArrayList<Map<String, Object>>();

				Map<String, Object> cInfo = null;
				List<Map<String, Object>> valueList = null;
				for (Map<String, Object> rvo : cList) {
					cInfo = new HashMap<String, Object>();
					cInfo.put("c_title", rvo.get("title"));
					cInfo.put("value", rvo.get("value"));
					
					cInfoList.add(cInfo);
				}
			}
			staff.put("c_info_list", cInfoList);

			result.put("staff", staff);
		} catch (Exception e) {
			if( logger != null){logger.error("Error", e);}
			//System.out.println(DateUtil.getYYYYMMDDhhmmss() + e.getMessage());
			result = null;
		}
		return result;
	}

	public Map<String, Object> getEpgChannel(Map<String, String> param) {

//		Map<String, Object> categoriesInfo = (Map<String, Object>)redisClient.hget(XpgCommon.EPG_CHANNELS, XpgCommon.EPG_CHANNELS);
//		List<Map<String, Object>> cList = (List<Map<String, Object>>)categoriesInfo.get("categories");
//		List<Map<String, Object>> cList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.GENRE_CHANNELS, XpgCommon.GENRE_CHANNELS);
		
		List<Map<String, Object>> cList = null;
		Object tempObj = redisClient.hget(XpgCommon.GENRE_CHANNELS, XpgCommon.GENRE_CHANNELS);
		if( tempObj instanceof List){
			cList = (List<Map<String, Object>>) tempObj;
		}

		List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
		String baseRegion = getRegions(param.get("region"));
		
		/*
		Map<String, Object> channel = null;
		for (int i = 0; i < cList.size(); i++) {
			channel = cList.get(i);
			 if(baseRegion.indexOf("#" + channel.get("cd_area") + "#") >= 0) {
				cateList.add(channel);
			}
		}
		 */
		
		Map<String, Object> channel = null;
		
		if( cList != null && !cList.isEmpty()){
//			System.out.println("cList: " + cList.toString());
			
			for (int i = 0; i < cList.size(); i++) {
				channel = cList.get(i);
				
				if( channel == null){
					continue;
				}
				
				List<Map<String, Object>> chList = null;
				Object tempObj2 = channel.get("channels");
				if( tempObj2 instanceof List){
					chList = (List<Map<String, Object>>) tempObj2;
				}
				
				// cd_area 에 속하는 채널만 남긴다.
				if( chList != null && chList.size() > 0){
					boolean isAddList = false;
					for( int j=0; j< chList.size(); j++){
						Map<String, Object> chMap = chList.get(j);
						
						if( chMap == null){
							continue;
						}
						
						if( baseRegion != null){
							if(baseRegion.indexOf("#" + chMap.get("cd_area") + "#") >= 0) {
								isAddList = true;
							}else{
								chList.remove(j);
							}
						}
					}
					
					// cd_area 에 속한 채널이 존재하면 리스트에 추가
					if( isAddList){
						if( channel != null){channel.put("channels", chList);}
						cateList.add(channel);
					}
				}
			}
		}else{
			if( logger != null)logger.info(">>>> cList is null or empty");
		}
		
		List<Map<String, Object>> categories = null;
		List<Map<String, Object>> channels = new ArrayList<Map<String, Object>>();
		Map<String, Object> category = new HashMap<String, Object>();
		Map<String, Object> channelItem = null;
		String categoryId = "";
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			if (cateList != null && cateList.size() > 0) {
//				System.out.println("cateList: " + cateList.toString());
				
				categories = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < cateList.size(); i++) {
					channelItem = cateList.get(i);

					/*
					if (channelItem != null && !categoryId.equals(channelItem.get("newui_gnr_cd"))) {
						if (i > 0) {
							category.put("t_cnt", String.valueOf(channels.size()));
							categories.add(category);
						}

						category = new HashMap<String, Object>();
						channels = new ArrayList<Map<String, Object>>();
						categoryId = (String) channelItem.get("newui_gnr_cd");

						// 예외처리 로직 추가필요
						category.put("category_id", categoryId);
						category.put("category_name", channelItem.get("newui_gnr_nm"));
						category.put("channels", channels);
					}

					channel = new HashMap<String, Object>();
					channel.put("service_id", channelItem.get("id_svc"));
					channel.put("channel_no", channelItem.get("no_ch"));
					channel.put("channel_name", channelItem.get("nm_ch"));
					channels.add(channel);
					*/
					
					String newuiGnrCd = (String) channelItem.get("newui_gnr_cd");
					String newuiGnrNm = (String) channelItem.get("newui_gnr_nm");
					List<Map<String, Object>> channelsList = (List<Map<String, Object>>) channelItem.get("channels");
					
					if (channelItem != null && !categoryId.equals(newuiGnrCd)) {
						
						
						if (i > 0) {
							category.put("t_cnt", String.valueOf(channels.size()));
							categories.add(category);
						}

						category = new HashMap<String, Object>();
						channels = new ArrayList<Map<String, Object>>();

						// 예외처리 로직 추가필요
						category.put("category_id", newuiGnrCd);
						category.put("category_name", newuiGnrNm);
						category.put("channels", channels);
					}

					if( channelsList != null && !channelsList.isEmpty()){
						for( Map<String, Object> chMap : channelsList){
							channel = new HashMap<String, Object>();
							channel.put("service_id", chMap.get("id_svc"));
							channel.put("channel_no", chMap.get("no_ch"));
							channel.put("channel_name", chMap.get("nm_ch"));
							channels.add(channel);
						}
					}
				}
				category.put("channels", channels);
				category.put("t_cnt", ((channels != null) ? (String.valueOf(channels.size())) : "0"));
				categories.add(category);
			}else{
				if( logger != null)logger.info(">>> cateList is null or empty");
			}

			if (categories != null) {
				result.put("result", "0000");
				result.put("reason", "");
				result.put("categories", categories);
				result.put("t_cnt", String.valueOf(categories.size()));
			} else {
				result.put("result", "7201");
				result.put("reason", "실시간채널 장르정보 없음");
			}
		} catch (Exception e) {
			if( logger != null){logger.error("Error", e);}

			result.put("result", "9999");
			result.put("reason", "ERROR");
		}
		return result;
	}

	private String getRegions(String regionCode) {
		StringBuilder areas = new StringBuilder("#0#");
		try {
			String [] regions = StringUtils.defaultIfEmpty(regionCode, "").split("\\^");
//			if(regions.length != 3) {
//				// 기본은 서울지역 방송 
//				areas.append("41#1#61#");
//			}
//			else {
//				for(int i = 0 ; i < regions.length ; i++) {
//					areas.append(regions[i].substring(regions[i].indexOf("=")+1) + "#");
//				}
//			}
			/**
			 * 2014.07.09 pcw 수정 
			 * UHD 컨텐츠 및 채널 추가로 인한 regions 코드가 3개이상 넘어올수 있음 
			 * ex) mbc=1^kbs=41^sbs=61^uhdmbc=100^uhdkbs=100^uhdsbs=100
			 **/
			if(regions.length >= 3) {
				for(int i = 0 ; i < regions.length ; i++) {
					areas.append(regions[i].substring(regions[i].indexOf("=")+1) + "#");
				}
			} else {
				// 기본은 서울지역 방송 
				areas.append("41#1#61#");
			}
		}
		catch(Exception e) {
			if( logger != null){logger.error("Error", e);}
//			e.printStackTrace();
			
			// 기본은 서울지역 방송 
			areas.append("41#1#61#");
		}
		
		return areas.toString();
	}
	
	// 실시간 채널순위 (AG-XP-00027:IF-XPG-APPD-007)
	@Override
	public Map<String, Object> getRealTimeChannel() {
//		Map<String, Object> result = (Map<String, Object>)redisClient.hget(XpgCommon.NXTCHANNELRATING, XpgCommon.NXTCHANNELRATING);
		Map<String, Object> result = null;
		Object tempObj = redisClient.hget(XpgCommon.NXTCHANNELRATING, XpgCommon.NXTCHANNELRATING);
		if( tempObj instanceof Map){
			result = (Map<String, Object>) tempObj;
		}
		if(result != null) {
			result.put("version", (String) redisClient.hget(XpgCommon.VERSION, XpgCommon.NXTCHANNELRATING));
			result.put("result", "0000");
        	result.put("reason", "");
        } else {
        	result = new HashMap<String, Object>();
        	result.put("result", "9999");
        	result.put("reason", "실시간 채널순위 데이터가 없습니다.");
        }
		return result;
	}
	
	// 인기VOD정보 (AG-XP-00028:IF-XPG-APPD-008)
	@Override
	public Map<String, Object> getGenreRankVodAndRefVod(Map<String, String> param) {
		String resltn_cd = param.get("resltn_cd");
		String category_id = StringUtils.defaultIfEmpty(param.get("category_id"),"");
		String con_id = param.get("con_id");
		String item = StringUtils.defaultIfEmpty(param.get("item"),"");
		String id_package = param.get("id_package");
		
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			// 인기 VOD 처리로직
			if("ranking".equals(item) || "".equals(item)) {
				Map<String, Object> rankings = (Map<String, Object>)redisClient.hget(XpgCommon.NXTPOPULARVOD, XpgCommon.NXTPOPULARVOD);
				
				if(rankings != null) {
					if(!"".equals(resltn_cd)) {
						List<Map<String, Object>> vod_rating_list = (List<Map<String, Object>>) rankings.get("vod_rating_list");
						if( vod_rating_list != null){
							for (Map<String, Object> v_r : vod_rating_list) {
								String poster = (String) v_r.get("poster");
								if(poster != null) {
									v_r.put("poster", getResltnPoster(resltn_cd, poster));
								}
							}
						}
					}
		        }
				result.put("rankings", rankings);
				if(rankings == null) {
					result = new HashMap<String, Object>();
					result.put("result", "9999");
					result.put("reason", "관련VOD 데이터가 없습니다.");
					return result;
				}
			}
		
			// 관련 VOD 처리로직
			if("relation".equals(item) || "".equals(item)) {
				Map<String, Object> relations = null;
				Map<String, Object> priceMap = null;
				
				if( !"".equals(category_id)) {
					relations = (Map<String, Object>)redisClient.hget(XpgCommon.NXT_GENRE_RELATION_VOD, category_id);	// 장르별 관련VOD (개별)
				}else {
					relations = (Map<String, Object>)redisClient.hget(XpgCommon.NXT_GENRE_RELATION_VOD_ALL, XpgCommon.NXT_GENRE_RELATION_VOD_ALL);	// 장르별 관련VOD (전체)
					con_id = "";
				}		
				
				if(relations != null) {					
					if(con_id != null && !"".equals(con_id)) {  // BBH 160802 : 파라미터값으로 컨텐츠아이디를 입력했을 경우 처리 로직
						Map<String, Object> org_vo = (Map<String, Object>) redisClient.hget(XpgCommon.CONTENTS, con_id);
						Map<String, Object> tmpMap = new HashMap<String, Object>();
						
						if(org_vo != null) {
							List<Map<String, Object>> priceList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.CONTENT_PRICE_LIST, con_id);
							String sePrice = "0"; // default
							if (priceList != null) {
								for (Map<String, Object> price : priceList) {
									if (price.containsKey(id_package)) {
										sePrice = price.get(id_package) + "";
									}
								}
							}
							
							List<Map<String, Object>> channel_genre_list = (List<Map<String, Object>>) relations.get("channel_genre_list");
							tmpMap.put("order", "1");
							tmpMap.put("level", StringUtils.defaultIfEmpty((String) org_vo.get("level"), "null"));
							tmpMap.put("percentage", StringUtils.defaultIfEmpty((String) org_vo.get("percentage"), "null"));
							tmpMap.put("yn_adult", StringUtils.defaultIfEmpty((String) org_vo.get("yn_adult"), "N"));
							tmpMap.put("g_code", StringUtils.defaultIfEmpty((String) org_vo.get("c_menu"), "null"));
							tmpMap.put("g_name", StringUtils.defaultIfEmpty((String) org_vo.get("g_name"), "null"));
							tmpMap.put("p_time", StringUtils.defaultIfEmpty((String) org_vo.get("p_time"), "null"));
							tmpMap.put("fg_quality", StringUtils.defaultIfEmpty((String) org_vo.get("fg_quality"), "null"));
							tmpMap.put("con_id", con_id);
							tmpMap.put("ser_no", StringUtils.defaultIfEmpty((String) org_vo.get("ser_no"), "null"));
							tmpMap.put("title", StringUtils.defaultIfEmpty((String) org_vo.get("title"), "null"));
							tmpMap.put("poster", StringUtils.defaultIfEmpty((String) org_vo.get("poster"), "null"));
							tmpMap.put("se_pri", StringUtils.defaultIfEmpty((String) sePrice, "0"));
							tmpMap.put("nor_pri", StringUtils.defaultIfEmpty((String) org_vo.get("nor_pri"), "0"));
							tmpMap.put("red_pri", StringUtils.defaultIfEmpty((String) org_vo.get("red_pri"), "0"));
							tmpMap.put("adv_red_amt", StringUtils.defaultIfEmpty((String) org_vo.get("adv_red_amt"), "null"));
							
							// resolution 처리
							if(  !"".equals(resltn_cd)    &&   org_vo.get("poster") != null  ) { // resolution 처리
//								String posterImg = (String)org_vo.get("poster");
//								tmpMap.put("poster", posterImg.replaceAll("\\.", "_" + resltn_cd + "."));
								tmpMap.put("poster", getResltnPoster(resltn_cd, (String) org_vo.get("poster")));
							}
							
							if("0".equals(org_vo.get("yn_fre"))) {
								tmpMap.put("yn_fre", "Y");
							}else {
								tmpMap.put("yn_fre", "N");
							}
							
							int order = 2;
							int vod_cnt;
							
							for (Map<String, Object> c_g : channel_genre_list) {
								List<Map<String, Object>> channel_genre_vod_list = (List<Map<String, Object>>) c_g.get("channel_genre_vod_list");							
								vod_cnt = 0;
								
								if( channel_genre_vod_list != null){
									for (Map<String, Object> c_g_v : channel_genre_vod_list) {								
										vod_cnt++;
										
										// resolution 처리
										if( !"".equals(resltn_cd) && c_g_v.get("poster") != null ) { // resolution 처리
//											String posterImg = (String)c_g_v.get("poster");
//											c_g_v.put("poster", posterImg.replaceAll("\\.", "_" + resltn_cd + "."));
											c_g_v.put("poster", getResltnPoster(resltn_cd, (String) c_g_v.get("poster")));
										}
										
										if( con_id.equals(c_g_v.get("con_id")) ) {  // 파라미터로 입력받은 컨텐츠가 channel_genre_vod_list에 있으면 channel_genre_vod_list에 있는 컨텐츠 order를 1로 바꾼다.
											//logger.info("VOD LIST에 파라미터로 입력받은 컨텐츠가 있음 : " + c_g_v.get("con_id") + "cnt : " + vod_cnt);
											for (Map<String, Object> c_g_v2 : channel_genre_vod_list) {
												if( con_id.equals(c_g_v2.get("con_id")) ) {
													//logger.info("[순서변경] 1순위로 가는 컨텐츠 : " + c_g_v2.get("con_id"));
													c_g_v2.put("order", "1");
												} else {
													//logger.info("[순서변경] 순위가 한단계씩 밀린 컨텐츠 : " + c_g_v2.get("con_id") + " ==> " + order);
													c_g_v2.put("order", String.valueOf(order++));									
												}
											}
											break;
										}
	
										// channel_genre_vod_list에 파라미터로 입력받은 컨텐츠가 없다면 기존 컨텐츠들의 order값을 1씩 증가시켜 둔다
										if(String.valueOf(vod_cnt).equals(c_g.get("total_count")) ) {
											for (Map<String, Object> c_g_v3 : channel_genre_vod_list) { // 기존 컨텐츠들의 order를 1씩 증가시킨다.
												//logger.info("[컨텐츠추가] 순위 한단계씩 미룸 : " + c_g_v3.get("con_id") + " ==> " + order);										
												c_g_v3.put("order", String.valueOf(order++));
											}			
											vod_cnt = 0;
										}								
										order = 2;
									}
									Collections.sort(channel_genre_vod_list, new GridComparator("order", 1, true));
								}
							}
							
							// channel_genre_vod_list에 없는 새로운 컨텐츠 추가
							String newContFlag = "N";
							for (Map<String, Object> c_g : channel_genre_list) {
								
								int totalCnt = Integer.parseInt(c_g.get("total_count").toString());
								if(totalCnt < 20) {
									totalCnt++;
									c_g.put("total_count", String.valueOf(totalCnt));
								}
								
								List<Map<String, Object>> channel_genre_vod_list = (List<Map<String, Object>>) c_g.get("channel_genre_vod_list");
								
								for (Map<String, Object> c_g_v : channel_genre_vod_list) {
									if( "2".equals(c_g_v.get("order")) ) {
										newContFlag = "Y";
									}
									break;
								}
								
								if( "Y".equals(newContFlag)) {
									channel_genre_vod_list.add(0, tmpMap);
									newContFlag = "N";
								}
								
								if( channel_genre_vod_list.size() > 20 ) {
									for (int index=21; index <= channel_genre_vod_list.size(); index++ ){
										channel_genre_vod_list.remove(index-1);
									}
								}
								Collections.sort(channel_genre_vod_list, new GridComparator("order", 1, true));
							}
						}
					}else{
						List<Map<String, Object>> channel_genre_list = (List<Map<String, Object>>) relations.get("channel_genre_list");
						for (Map<String, Object> c_g : channel_genre_list) {
							List<Map<String, Object>> channel_genre_vod_list = (List<Map<String, Object>>) c_g.get("channel_genre_vod_list");							
							if( channel_genre_vod_list != null){
								for (Map<String, Object> c_g_v : channel_genre_vod_list) {								
									// resolution 처리
									if( !"".equals(resltn_cd) && c_g_v.get("poster") != null ) { // resolution 처리
										c_g_v.put("poster", getResltnPoster(resltn_cd, (String) c_g_v.get("poster")));
									}
								}
							}
						}
					}
				}
				
				result.put("relations", relations);
				if(relations == null) {
					result = new HashMap<String, Object>();
					result.put("result", "9999");
					result.put("reason", "관련VOD 데이터가 없습니다.");
					return result;
				}
			}	
	
			result.put("version", (String) redisClient.hget(XpgCommon.VERSION, XpgCommon.NXT_GENRE_RELATION_VOD));
			result.put("result", "0000");
			result.put("reason", "");
		}catch(NumberFormatException nfe) {
			if( logger != null){logger.error("Error", nfe);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR(number format)");
		}catch(NullPointerException ne) {
			if( logger != null){logger.error("Error", ne);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR(null)");
		}catch(Exception e){
			if( logger != null){logger.error("Error", e);}
			result = new HashMap<String, Object>();
			result.put("result", "9999");
			result.put("reason", "ERROR");
		}			
		return result;
	}
	
	public String getResltnPoster( String resltnCd, String posterImg){
		return posterImg.replaceAll("\\.", "_" + resltnCd + ".");
	}
	
	// 검색지정 컨텐츠 (AG-XP-00029:IF-XPG-APPD-009)
	@Override
	public Map<String, Object> getSpecifyContents(Map<String, String> param) {
		Map<String, Object> result = new HashMap<>();

//		List<Map<String, Object>> specifyContentsList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.SEARCH_TARGET_CONTENTS, "specifyContents");
		List<Map<String, Object>> specifyContentsList = null;
		Object tempObj = redisClient.hget(XpgCommon.SEARCH_TARGET_CONTENTS, "specifyContents");
		if( tempObj instanceof List){
			specifyContentsList = (List<Map<String, Object>>) tempObj;
		}
		
		if (specifyContentsList != null) {
			String resltnCd = param.get("resltn_cd");
			String idPackage = param.get("id_package");

			for (Map<String, Object> contentMap : specifyContentsList) {
				if( contentMap == null){
					continue;
				}
				
				String posterImg = StringUtils.defaultIfEmpty((String) contentMap.get("poster"), "");

				if (!"".equals(resltnCd)) {
					String temp1 = "";
					if( posterImg != null){temp1 = posterImg.replace(".", "_" + resltnCd + ".");}
					contentMap.put("poster", temp1);
				}

				// 가격정보 넣기
				String conId = (String) contentMap.get("con_id");
//				List<Map<String, Object>> priceList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.CONTENT_PRICE_LIST, conId);
				List<Map<String, Object>> priceList = null;
				Object tempObj2 = redisClient.hget(XpgCommon.CONTENT_PRICE_LIST, conId);
				if( tempObj2 instanceof List){
					priceList = (List<Map<String, Object>>) tempObj2;
				}
				
				String sePrice = "0"; // default
				if (priceList != null) {
					for (Map<String, Object> price : priceList) {
						if (price.containsKey(idPackage)) {
//							sePrice = (Long) price.get(idPackage) + "";
							Object tempObj3 = price.get(idPackage);
							if( tempObj3 instanceof Long){
								sePrice = tempObj3 + "";
							}
						}
					}
				}
				contentMap.put("se_pri", sePrice);

//				Map<String, Object> contentsMap = (Map<String, Object>) redisClient.hget(XpgCommon.CONTENTS, conId);
				Map<String, Object> contentsMap = null;
				Object tempObj4 = redisClient.hget(XpgCommon.CONTENTS, conId);
				if( tempObj instanceof Map){
					contentsMap = (Map<String, Object>) tempObj4;
				}
				
				String cMenu = "";
				if( contentsMap != null){ cMenu = (String) contentsMap.get("c_menu");}
//				Map<String, Object> attributesMap = (Map<String, Object>) redisClient.hget(XpgCommon.ATTRIBUTES, cMenu);
//				String cdAttr = (String) attributesMap.get("cd_attribute");
//
//				logger.info("cd_attributes : " + cdAttr);
//
//				if ("4".equals(cdAttr)) {
//					sePrice = "월정액";
//				}
//
//				contentMap.put("se_pri", sePrice);

				// return 하지 않는 element 삭제
				contentMap.remove("priority");
				contentMap.remove("yn_use");
			}

			result.put("contents", specifyContentsList);
			result.put("result", "0000");
			result.put("reason", "");
		} else {
			result.put("result", "7073");
			result.put("reason", "CI 메모리에 데이터 없음");
		}

		return result;
	}
	
	// 코너 데이터 (AG-XP-00024:IF-XPG-APPD-010)
	@Override
	public Map<String, Object> getCorner(Map<String, String> param) {
		Map<String, Object> result = new HashMap<>();
		
		String epiGid = param.get("epi_gid");
		
		try{
			List<Map<String, Object>> cornerList = (List<Map<String, Object>>) redisClient.hget(XpgCommon.CORNER_GROUP_CONTENTS, epiGid);
			Map<String, Object> content = (Map<String, Object>) redisClient.hget(XpgCommon.CONTENTS, epiGid);
			if(cornerList != null) {
				for (Map<String, Object> corner : cornerList) {
					content = (Map<String, Object>) redisClient.hget(XpgCommon.CONTENTS, corner.get("con_id").toString());
					if (content != null) {
						corner.put("ser_no", content.get("ser_no") + "");	
					}
				}
				
				result.put("corner_list", cornerList);
				result.put("result", "0000");
				result.put("reason", "");
			} else {
				result.put("result", "7073");
				result.put("reason", resultCommon.resultCodeListMap.get("7073"));
			}
		}catch (Exception e){
			if( logger != null){logger.error("Error", e);}
//			e.printStackTrace();
			
			result.put("result", "9999");
			result.put("reason", resultCommon.resultCodeListMap.get("9999"));
		}
		
		return result;
	}
	
}
