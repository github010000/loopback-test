package com.skb.xpg.appd.svc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.svc.XpgAppdService;
import com.skb.xpg.appd.svc.util.CommonUtils;
import com.skb.xpg.appd.svc.util.StringUtils;

@RestController
public class XpgAppdController {
	private static final Logger logger = LoggerFactory.getLogger(XpgAppdController.class.getName());
	
	@Autowired
	private XpgAppdService ntstbService;

	@Autowired
	RedisClient redisClient;
	
	@Autowired
	private CommonUtils commonUtil;
	
	
	/**
	 * 홈배너 Home Menu Bannel (AG-XP-00021:IF-XPG-APPD-001)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/home/banner")	
	public Map<String, Object> getHomeMenuBanner(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
		// logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName	= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat = StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		
		String menuId	= StringUtils.defaultIfEmpty(request.getParameter("menu_id"), "");
		String uiName	= StringUtils.defaultIfEmpty(StringUtils.upperCase(request.getParameter("ui_name")), "NXNEWUI"); // 기본값은 신규UI
		String promResltnCd	= StringUtils.defaultIfEmpty(request.getParameter("prom_resltn_cd"), ""); // 프로모션 이미지 해상도
		
		String stbId	= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-001", "RECV.REQ", stbId, request.getQueryString()));
		
		String[] essParamArray = {"IF", "response_format", "id_package", "menu_id", "stb_id", "ui_name", "prom_resltn_cd"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("ui_name", uiName);
		param.put("menu_id", menuId);
		param.put("prom_resltn_cd",	promResltnCd);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result = ntstbService.getHomeMenuBanner(param);
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 전체메뉴 배너 (AG-XP-00022:IF-XPG-APPD-002)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/allmenu/banner")
	public Map<String, Object> getAllMenuBanner(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
		// logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();			
		
		String ifName		= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat = StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName		= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "NXNEWUI"); // 기본값은 신규UI
		String serviceCode	= StringUtils.defaultIfEmpty(request.getParameter("service_code"), "0");
		String idPackage	= StringUtils.defaultIfEmpty(request.getParameter("id_package"), "");
		String promTopCd	= StringUtils.defaultIfEmpty(request.getParameter("prom_top_cd"), ""); // 상단 프로모션 이미지 해상도
		String promBottomCd	= StringUtils.defaultIfEmpty(request.getParameter("prom_bottom_cd"), ""); // 하단 프로모션 이미지 해상도
		
		String stbId		= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), ""); // 하단 프로모션 이미지 해상도
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-002", "RECV.REQ", stbId, request.getQueryString()));
		
		Map<String, Object> result = null;
		
		String[] essParamArray = {"IF", "response_format", "ui_name", "service_code", "id_package", "menu_id", "stb_id"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("ui_name", uiName);
		param.put("service_code", serviceCode);
		param.put("id_package", idPackage);
		param.put("prom_top_cd", promTopCd);
		param.put("prom_bottom_cd", promBottomCd);
		
		result = ntstbService.getPromoMenu(param);
		result.put("IF", ifName);
		result.put("ver", ver);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 컨텐츠 별 평점 및 부가정보 (AG-XP-00023:IF-XPG-APPD-003)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/content/pntEpiInfo")
	public Map<String, Object> getContentPntEpiInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
		// logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName			= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat	= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName 			= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "NXNEWUI"); // 기본값은 신규UI
		String conId	 		= StringUtils.defaultIfEmpty(request.getParameter("con_id"), "");
		String stCd				= StringUtils.defaultIfEmpty(request.getParameter("st_cd"), ""); // 스틸컷 해상도
		String stBigCd 			= StringUtils.defaultIfEmpty(request.getParameter("st_b_cd"), ""); // 큰 스틸컷 해상도
		String heroCd			= StringUtils.defaultIfEmpty(request.getParameter("hero_cd"), "");
		String epiResltnCd		= StringUtils.defaultIfEmpty(request.getParameter("epi_resltn_cd"), "");
		String epiScrResltnCd	= StringUtils.defaultIfEmpty(request.getParameter("epi_scr_resltn_cd"), "");
		String cmtCd			= StringUtils.defaultIfEmpty(request.getParameter("cmt_cd"), "");
		String cmtBigCd			= StringUtils.defaultIfEmpty(request.getParameter("cmt_b_cd"), "");

		String stbId			= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-003", "RECV.REQ", stbId, request.getQueryString()));
		
		// content_id가 잘못 올라오는것이 있어서 trim
		if (conId.length() > 38) {
			conId = conId.trim();
		}
		
		Map<String, Object> result = null;
		
		String[] essParamArray = {"IF", "response_format", "ui_name", "id_package", "con_id", "stb_id", "st_cd", "st_b_cd", "pre_cd", "pre_b_cd", "hero_cd", "hash_id", "epi_resltn_cd"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("content_id", conId);
		param.put("st_cd", stCd);
		param.put("st_b_cd", stBigCd);
		param.put("hero_cd", heroCd);
		param.put("epi_resltn_cd", epiResltnCd);
		param.put("epi_scr_resltn_cd", epiScrResltnCd);
		param.put("cmt_cd", cmtCd);
		param.put("cmt_b_cd", cmtBigCd);
		
		result = ntstbService.getContentPntEpiInfo(param);
		
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 연관컨텐츠 (AG-XP-00024:IF-XPG-APPD-004)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/content/refInfo")
	public Map<String, Object> getContentRefInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName			= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat	= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName 			= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "NXNEWUI"); // 기본값은 신규UI
		String conId	 		= StringUtils.defaultIfEmpty(request.getParameter("con_id"), "");
		String linkResltnCd		= StringUtils.defaultIfEmpty(request.getParameter("link_resltn_cd"), "");
		String rstType			= StringUtils.defaultIfEmpty(request.getParameter("rst_Type"), "");
		
		String stbId			= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-004", "RECV.REQ", stbId, request.getQueryString()));
		
		// content_id가 잘못 올라오는것이 있어서 trim
		if (conId.length() > 38) {
			conId = conId.trim();
		}
		
		String[] essParamArray = {"IF", "response_format", "ui_name", "id_package", "con_id", "stb_id", "link_resltn_cd", "rst_type"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("contentId", conId);
		param.put("link_resltn_cd", linkResltnCd); // 관련컨텐츠 포스터 해상도
		param.put("rst_type", rstType); // 해상도 구분
		
		Map<String, Object> result = ntstbService.getContentRefInfo(param);
		
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 인물정보 (AG-XP-00025:IF-XPG-APPD-005)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/content/staffInfoList")
	public Map<String, Object> getStaffInfoList(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName			= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat 	= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName			= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "NEWUI"); // 기본값은 신규UI
		
		String idPackage		= StringUtils.defaultIfEmpty(request.getParameter("id_package"), "20");	// 기본값은 20
		String id				= StringUtils.defaultIfEmpty(request.getParameter("id"), "");
		String resltnCd			= StringUtils.defaultIfEmpty(request.getParameter("resltn_cd"), "");
		
		String stbId			= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-005", "RECV.REQ", stbId, request.getQueryString()));
		
		String[] essParamArray = {"IF", "response_format", "id_package", "id", "resltn_cd"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		param.put("packageId", idPackage);
		param.put("resltn_cd", resltnCd);
		
		Map<String, Object> result = ntstbService.getStaffInfoList(param);
		result.put("IF", ifName);
		result.put("ver", ver);		
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;	
	}
	
	/**
	 * NextUI 실시간장르 편성정보 (IF-XPG-119)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/{ver}/epg/channel")
	public Map<String, Object> getNextEpgCategoryInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		long start_time  = System.currentTimeMillis();
		String request_time = getDateWithMilliSecond();
		
		String format 		= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String IF			= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
//		String ver			= StringUtils.defaultIfEmpty(request.getParameter("ver"), "");
		String ui_name		= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "NEWUI"); // 기본값은 신규UI
		String region		= StringUtils.defaultIfEmpty(request.getParameter("region_code"), "");
		String track_id 	= StringUtils.defaultIfEmpty(request.getParameter("track_id"), ""); // trackID - CW DataCollector에 사용 (김현M 요청 - 17.05.11 hojoon90)
		
		String stbId		= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), ""); 
		
		logger.debug(StringUtils.getLogString("IF-XPG-119", "RECV.REQ", stbId, request.getQueryString()));
		
		Map<String, Object> result = null;
		
		if("".equals(ui_name) || "".equals(format) || "".equals(IF) || "".equals(ver))  {
			result = new HashMap<String, Object>();
			result.put("IF", IF);
			result.put("ver", ver);
			result.put("result", "9001");
			result.put("reason", "입력값 오류");
			return result;
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("region", region);
		
//		result = stbService.getEpgCategoryMappingInfo(param);
		result = ntstbService.getEpgChannel(param);
		result.put("IF", IF);
		result.put("ver", ver);		
		result.put("ui_name", ui_name);
		
//		long end_time = System.currentTimeMillis();   
//        double run_time = (end_time - start_time)/1000.0; 
//        logger.info("[IF: getContentAndRef]" +
//        		"[Start Time:" + start_time +"]" +
//		   		"[End Time:" + end_time +"]" +
//		   		"[Run_Time:" + run_time +"]" );
//        result.put("response_time", run_time+"");
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	
	/**
	 * 실시간 채널순위 (AG-XP-00027:IF-XPG-APPD-007)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/realTime/channel")
	public Map<String, Object> getRealTimeChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		logger.debug("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName			= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat	= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName			= StringUtils.defaultIfEmpty(StringUtils.upperCase(request.getParameter("ui_name")), ""); // 기본값은 신규UI

		String stbId			= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-007", "RECV.REQ", stbId, request.getQueryString()));
		
		String[] essParamArray = {"IF", "response_format", "id_package"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, Object> result = ntstbService.getRealTimeChannel();
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 인기VOD정보 (AG-XP-00028:IF-XPG-APPD-008)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/popular/vodInfo")
	public Map<String, Object> getGenreRankVodAndRefVod(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		logger.debug("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName		= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat = StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName		= StringUtils.defaultIfEmpty(StringUtils.upperCase(request.getParameter("ui_name")), ""); // 기본값은 신규UI
		String idPackage	= StringUtils.defaultIfEmpty(request.getParameter("id_package"), "");
		String resltnCd		= StringUtils.defaultIfEmpty(request.getParameter("resltn_cd"), "");
		String categotyId	= StringUtils.defaultIfEmpty(request.getParameter("category_id"), "");
		String item			= StringUtils.defaultIfEmpty(request.getParameter("item"), "");
		String conId		= StringUtils.defaultIfEmpty(request.getParameter("con_id"), "");

		String stbId		= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-008", "RECV.REQ", stbId, request.getQueryString()));
		
		String[] essParamArray = {"IF", "response_format", "id_package", "rst_type", "stb_id"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("resltn_cd", resltnCd);
		param.put("category_id", categotyId);
		param.put("con_id", conId);
		param.put("item", item);
		param.put("id_package", idPackage);
		
		Map<String, Object> result = ntstbService.getGenreRankVodAndRefVod(param);
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 검색지정 컨텐츠 (AG-XP-00029:IF-XPG-APPD-009)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/content/specify")
	public Map<String, Object> getSpecifyContents(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName		= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat 	= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName		= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "");
		
		String idPackage	= StringUtils.defaultIfEmpty(request.getParameter("id_package"), "");
		String resltnCd		= StringUtils.defaultIfEmpty(request.getParameter("resltn_cd"), "");
		
		String stbId		= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-009", "RECV.REQ", stbId, request.getQueryString()));
		
		String[] essParamArray = {"IF", "response_format", "id_package", "stb_id", "ui_version", "resltn_cd"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("id_package", idPackage);
		param.put("resltn_cd", resltnCd);
		
		Map<String, Object> result = ntstbService.getSpecifyContents(param);
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	/**
	 * 코너별 모아보기 (AG-XP-00030:IF-XPG-APPD-010)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{ver}/content/corner")
	public Map<String, Object> getCorner(HttpServletRequest request, HttpServletResponse response, @PathVariable String ver) throws Exception {
//		logger.info("[REQ(수신)] " + request.getQueryString());
		String request_time = getDateWithMilliSecond();
		
		String ifName		= StringUtils.defaultIfEmpty(request.getParameter("IF"), "");
		String responseFormat 	= StringUtils.defaultIfEmpty(request.getParameter("response_format"), "json");
		String uiName		= StringUtils.defaultIfEmpty(request.getParameter("ui_name"), "BSUHD2"); // 기본값은 신규UI
		String idPackage	= StringUtils.defaultIfEmpty(request.getParameter("id_package"), "");
		String resltnCd		= StringUtils.defaultIfEmpty(request.getParameter("resltn_cd"), "");
		String stbId		= StringUtils.defaultIfEmpty(request.getParameter("stb_id"), "");
		String uiVersion		= StringUtils.defaultIfEmpty(request.getParameter("ui_version"), "");
		String epiGid		= StringUtils.defaultIfEmpty(request.getParameter("epi_gid"), "");
		
		logger.debug(StringUtils.getLogString("IF-XPG-APPD-010", "RECV.REQ", stbId, request.getQueryString()));
		
		String[] essParamArray = {"IF", "response_format", "ui_name", "id_package", "stb_id"};
		if( !commonUtil.validCheck(request, essParamArray)){
			return commonUtil.returnError(ifName, ver, "9001");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("id_package", idPackage);
		param.put("resltn_cd", resltnCd);
		param.put("epi_gid", epiGid);
		
		Map<String, Object> result = ntstbService.getCorner(param);
		result.put("IF", ifName);
		result.put("ver", ver);			
		result.put("ui_name", uiName);
		
		String response_time = getDateWithMilliSecond();
		result.put("request_time", request_time);
		result.put("response_time", response_time);
		
		return result;
	}
	
	public String getDateWithMilliSecond() {  // 20160824 BBH - request/response time 기록용
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		return dayTime.format(new Date(time));		
	}
}
