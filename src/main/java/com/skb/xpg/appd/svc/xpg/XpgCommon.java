package com.skb.xpg.appd.svc.xpg;

import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.util.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
//@Configuration
public class XpgCommon {
	public final static String __HASH		= "HASH";
	public final static String __STRING		= "STRING";
	public final static String __newest		= "newest";
	public final static String __shopping	= "shopping";
	public final static String __eros		= "eros";
	public final static String __null		= "null";
	public final static String __1			= "1";
	public final static String __2			= "2";
	public final static String __10			= "10";
	public final static String __20			= "20";
	public final static String __30			= "30";
	public final static String __100		= "100";
	
	private Environment runtimeProperties;
	private RedisClient redisClient;

	public XpgCommon(Environment runtimeProperties, RedisClient redisClient) {
		this.runtimeProperties = runtimeProperties;
		this.redisClient = redisClient;
		
		//유저별 상품팩
		servicePackNo = new HashMap<>();
		servicePackNo.put("100#10", "100#10");
		servicePackNo.put("100#15", "100#15");
		servicePackNo.put("100#20", "100#20");
		servicePackNo.put("101#10", "101#10");
		servicePackNo.put("101#15", "101#15");
		servicePackNo.put("102#10", "102#10");
		servicePackNo.put("102#20", "102#20");
		servicePackNo.put("103#15", "103#15");
		servicePackNo.put("103#20", "103#20");
		servicePackNo.put("104#10", "104#10");
		servicePackNo.put("105#15", "105#15");
		servicePackNo.put("106#20", "106#20");
	}
	
	public final static String ALL_MENUS = "AllMenus";
    public final static String HOME_ALL_MENUS = "HomeAllMenus";
    public final static String HOME_MY_BTV_MENUS = "HomeMyBtvMenus";
    public final static String ETC_MENUS = "EtcMenus";
    public final static String LIB_MENUS = "LibMenus";
    public final static String HELP_MENUS = "HelpMenus";
    public final static String HOME_MY_PPMMENUS = "HomeMyPPMMenus";
    public final static String PROMO_PARENT_BY_HOME_MENUS = "PromoParentByHomeMenus";
    public final static String MY_PPMPARENT_BY_HOME_MENUS = "MyPPMParentByHomeMenus";
    public final static String PROMO_BY_HOME_MENUS = "PromoByHomeMenus";
    public final static String PROMOTION = "Promotion";
    public final static String GENRE_PROMO = "GenrePromo";
    public final static String MY_PPMBY_HOME_MENUS = "MyPPMByHomeMenus";
    public final static String GENRE_PROMOTION_PARENT_MENUS = "GenrePromotionParentMenus";
    public final static String LIBRARY_SYMBOLIC_LINK_MENUS = "LibrarySymbolicLinkMenus";
    public final static String GRID = "Grid";
    public final static String HOME_GRID = "HomeGrid";
    public final static String ISSUE_NSTAR_GRID = "IssueNStarGrid";
    public final static String LIB_GRID = "LibGrid";
    public final static String ISSUE_STAR_MENUS = "IssueStarMenus";
    public final static String PREVIEW = "Preview";
    public final static String EPG_CHANNELS = "EpgChannels";
    public final static String GENRE_CHANNELS = "GenreChannels";
    public final static String COMMENTARY_IMAGE = "CommentaryImage";
    public final static String MID_GRID = "MidGrid";
    public final static String MID_HOME_GRID = "MidHomeGrid";
    public final static String MID_LIB_GRID = "MidLibGrid";
    public final static String CON_BY_PID = "ConByPID";
    public final static String CONTENTS_LIST = "ContentsList";
    public final static String EXPIRE_CONTENTS_LIST = "ExpireContentsList";
    public final static String SEARCH_TARGET_CONTENTS = "SearchTargetContents";
    public final static String TOP_BOTTOM_RELATION_CONTENTS = "TopBottomRelationContents";
    public final static String FIRST_SERIES_CONTENTS = "FirstSeriesContents";
    public final static String BANNER_POSTER_TYPES = "BannerPosterTypes";
    public final static String META_PRSN_INFO = "MetaPrsnInfo";
    public final static String META_INFO = "MetaInfo";
    public final static String PARTAIL_META_INFO = "PartailMetaInfo";
    public final static String CHANNEL = "Channel";
    public final static String SERIES_QUALITY = "SeriesQuality";
    public final static String CORNER_LIST = "CornerList";
    public final static String CONTENT_PRICE_LIST = "ContentPriceList";
    public final static String CORNER_GROUP_CONTENTS = "CornerGroupContents";
    public final static String CONTENTS = "Contents";
    public final static String MID_TO_CID_LIST = "MidToCidList";
    public final static String CID_TO_MID = "CidToMid";
    public final static String PACKAGE_PRODUCT_ONLY_UIINFOS = "PackageProductOnlyUIInfos";

    public final static String INTG_MAP = "IntgMap";
    public final static String META_CONTENTS = "MetaContents";
    
    public final static String CONTENTS_SERIES = "ContentsSeries";
    
//  public final static String VODSNEW = "VodsNew";
    public final static String VODSNEW = "MetaInfo";		// 기존 VodsNew 콜렉션의 타겟이 변경됨
//  public final static String PEOPLESNEW = "PeoplesNew";
    public final static String PEOPLESNEW = "MetaPrsnInfo";	// 기존 PeoplesNew 콜렉션의 타겟이 변경됨
    public final static String PAR_VODSNEW = "PartialVodsNew";
    public final static String PAR_PEOPLESNEW = "PatialPeoplesNew";
    
    public final static String EPI_N_EPI = "epiNepi";
    public final static String EPI_N_EPI_CON = "epiNepiCon";
    
//	//NextUI 실시간 채널순위 Data (From 디지캡)
	public final static String NXTCHANNELRATING						= "nxtChannelRating";
	//NextUI 인기 VOD Data (From 디지캡)
	public final static String NXTPOPULARVOD						= "nxtPopularVod";
//	//NextUI 관련 VOD Data (From 디지캡)
	public final static String NXTRELATIONVOD						= "nxtRelationVod";
	//NextUI 장르별 관련VOD Data (From 디지캡)
	public final static String NXT_GENRE_RELATION_VOD				= "nxtGenreRelationVod";
	//NextUI 장르별 관련VOD All Data (From 디지캡)
	public final static String NXT_GENRE_RELATION_VOD_ALL			= "nxtGenreRelationVodAll";
	
	// RatingNAdditionalDataWorker
	public final static String M_VODS_MAP							= "MidVodsMap";
	public final static String BTV_MOV_POINT						= "BtvMovPoint";
	
    public final static String ALL_MENU_BANNER = "AllMenuBanner";
    public final static String ALL_MENU_CATEGORY_BANNER = "AllMenuCategoryBanner";
    public final static String HOME_BANNER = "HomeBanner";
    public final static String ONE_DEPTH_BANNER = "OneDepthBanner";

	// Version
	public final static String VERSION = "Version";
	public final static String META_VERSION = "MetaVersion";

	// NextUI 그리드 타입
	public final static String NXT_GRID_TYPE_1 = "all"; // 전체메뉴의 그리드
	public final static String NXT_GRID_TYPE_2 = "home"; // (Home)메뉴의 그리드
	public final static String NXT_GRID_TYPE_3 = "issue"; // 이슈&스타의 그리드
	
	
	//통합오퍼링 관련 코드값,1:xpg 팝업,2:browse,3:실시간사내방송,4:로컬경로,5:http통신,6:내부고정,12:에코시스템
	public static final String CALL_TYPE_1 = "1";
	public static final String CALL_TYPE_2 = "2";
	public static final String CALL_TYPE_3 = "3";
	public static final String CALL_TYPE_4 = "4";
	public static final String CALL_TYPE_5 = "5";
	public static final String CALL_TYPE_6 = "6";
	public static final String CALL_TYPE_12 = "12";
	
	//통합오퍼링 관련 코드값 : 타입코드(0:내부이동,1:예고편보기,2:리뷰쓰기,3:관련시리즈보기,4:자막음성설정,5:통합오퍼링 리스트,6:통합오퍼링 상세,7:시청하기,8:관련상품보기,9:관심국가등록,
	//							10:바로구매,11:관련이미지,12:시놉정보보기,13:실시간중계방송보기(게임),14:관심GJ등록하기,15:게임프로모션영상,16:다른GJ영상보기,17:인물정보보기,
	//							18:게임vod리스트보기,19:팝업공지내용출력,20:내부 이미지링크,21:조이이동,22:검색,23:전체메뉴출력,24:브라우저타입,25:채널매니저,26:게임VOD 시청하기,
	//							27:SMS 발송,28:코너별시청,29:무한시청(HD),30:HD구매,31:SD구매,32:원격uiapp,33:무한시청(SD)),38:소장용(SD),39:소장용(HD),40:소장용(UHD)
	public static final String LINK_TYPE_1 = "1";
	public static final String LINK_TYPE_2 = "2";
	public static final String LINK_TYPE_3 = "3";
	public static final String LINK_TYPE_4 = "4";
	public static final String LINK_TYPE_5 = "5";
	public static final String LINK_TYPE_6 = "6";
	public static final String LINK_TYPE_7 = "7";
	public static final String LINK_TYPE_8 = "8";
	public static final String LINK_TYPE_9 = "9";
	public static final String LINK_TYPE_10 = "10";
	public static final String LINK_TYPE_11 = "11";
	public static final String LINK_TYPE_12 = "12";
	public static final String LINK_TYPE_13 = "13";
	public static final String LINK_TYPE_14 = "14";
	public static final String LINK_TYPE_15 = "15";
	public static final String LINK_TYPE_16 = "16";
	public static final String LINK_TYPE_17 = "17";
	public static final String LINK_TYPE_18 = "18";
	public static final String LINK_TYPE_19 = "19";
	public static final String LINK_TYPE_20 = "20";
	public static final String LINK_TYPE_21 = "21";
	public static final String LINK_TYPE_22 = "22";
	public static final String LINK_TYPE_23 = "23";
	public static final String LINK_TYPE_24 = "24";
	public static final String LINK_TYPE_25 = "25";
	public static final String LINK_TYPE_26 = "26";
	public static final String LINK_TYPE_27 = "27";
	public static final String LINK_TYPE_28 = "28";
	public static final String LINK_TYPE_29 = "29";
	public static final String LINK_TYPE_30 = "30";
	public static final String LINK_TYPE_31 = "31";
	public static final String LINK_TYPE_32 = "32";
	public static final String LINK_TYPE_33 = "33";
	public static final String LINK_TYPE_34 = "34";
	public static final String LINK_TYPE_35 = "35";
	public static final String LINK_TYPE_36 = "36";
	public static final String LINK_TYPE_37 = "37";
	public static final String LINK_TYPE_38 = "38";
	public static final String LINK_TYPE_39 = "39";
	public static final String LINK_TYPE_40 = "40";
	
	public final static String DEFAULT_MENU_IMAGE_STB_PATH 	= "/DATA/epg/menu_image/update/";
	
	public final static String DEFAULT_GNB_LIST_PATH		= "/svc/xpg/was/config/gnb_list.txt";
	
	public final static String DEFAULT_STORY_LONG_DESC	= "[OK]버튼을 누르시면 상세 시놉화면으로 이동 합니다.";
	
	//HOME 버튼 기본 상태 
//	private Map<String, Object> HOME_MENU_STATE;
	
	/////////// 2014.04.04 pcw 작업, mail : 지상파, CJ E&M 시놉시스 수정 관련의 건
	public Boolean isUseHoldback = false;
	
	// 기본적으로 모든 셋탑에대해 필터링한다. 
	public final static String STB_MODEL_FOR_ALL_FILTERING	= "!@#$%^&";
	
//	private String curMGRedisIP;
	
	//2016.08.19 cwkim RedisCluster Enable/Disable
	private boolean bUseRedisCluster = true;
	
//	private XpgCommon() {
//		//유저별 상품팩
//		servicePackNo = new HashMap<>();
//		servicePackNo.put("100#10", "100#10");
//		servicePackNo.put("100#15", "100#15");
//		servicePackNo.put("100#20", "100#20");
//		servicePackNo.put("101#10", "101#10");
//		servicePackNo.put("101#15", "101#15");
//		servicePackNo.put("102#10", "102#10");
//		servicePackNo.put("102#20", "102#20");
//		servicePackNo.put("103#15", "103#15");
//		servicePackNo.put("103#20", "103#20");
//		servicePackNo.put("104#10", "104#10");
//		servicePackNo.put("105#15", "105#15");
//		servicePackNo.put("106#20", "106#20");
//	}
	
	public String get(String pName) {
		return runtimeProperties.getProperty(pName);
	}
/*
	public void set(String key , Object value){
		runtimeProperties.setProperty(key, value);
	}*/
	
	/* 고객센터 전화번호 */
//    public static String CALLCENTER_PHONE = "1600-1533";
     

    /* 신용정보 점검 관련 상수 시작  */
    //  정기점검시  MenuLinkTag.isCB_DB_CHECK를 true로 셋팅되었는지 확인하고, 
    // CodeConstant.CB_CB_CHECK_STARTDATE, CodeConstant.CB_CB_CHECK_ENDDATE 변수에 날짜를 셋팅한후,
    // CodeConstant.CB_CB_CHECK_MSG 메시지를 적절히 수정한다.
    // 메인화면(creditbankMain.jsp) 비회원조회 및 다른 서비스에서 신용조회관련 항목이 막혔는지 확인.
    // CodeConstant.CB_CB_CHECK_MSG 메시지 날짜 수정
    // javascript:checkDateBeforeRun(url) 함수를 사용하면 jsp에서 특별한 조치없이 신용조회를 막을수 있다.
//    public static String CB_CB_CHECK_MSG 			= "신용정보 서비스 정기점검 관계로 신용정보 조회서비스가 일시 중단됩니다.(인터넷가입정보서비스는 정상운영) \\n일시 : 2007/02/11(일) 00:00 ∼ 04:00\\n서비스 이용에 불편을 드려 대단히 죄송합니다.\\n회원님의 많은 양해 바라며 더 나은 서비스를 위해 노력하겠습니다.";
//    public static String CB_CB_CHECK_STARTDATE 		= "200701110000"; // 월은 원래값 -1 해야 함에 주의!! 예) 2006년 1월 14일 오후 4시 00초 -> 200600140400
//    public static String CB_CB_CHECK_ENDDATE 	    = "200701110400";
    
    /* 신용정보 점검 관련 상수 끝  */	
	
	public static final String SERVER_RESOURCE_KEY = "SERVER_RESOURCE_KEY";
	
	/* 권한코드값 시작 */
	public static final int MASTERLEVEL = 1;	// Master 관리자 
	public static final int STAFFLEVEL = 2;		// Staff 관리자
	public static final int CONFIRMLEVEL = 3;	// 본인확인된 회원 (정회원)
	public static final int MEMBERLEVEL = 4;	// 일반회원
	public static final int PREMIUMLEVEL = 5;	// 프리미엄 회원
	/* 권한코드값 끝 */

    public static final int MAX_LIST_CNT = 30;
    
	/* 신고이유 시작*/
	public static final String CLAIMRSN_1 = "01";
	public static final String CLAIMRSN_2 = "02";
	public static final String CLAIMRSN_3 = "03";
	public static final String CLAIMRSN_4 = "04";
	public static final String CLAIMRSN_5 = "05";
	public static final String CLAIMRSN_6 = "06";
	public static final String CLAIMRSN_7 = "07";
	public static final String CLAIMRSN_8 = "08";
	public static final String CLAIMRSN_9 = "09";
	public static final String CLAIMRSN_10 = "10";
	
	
	public static final String M_CONTENTS_MAP = "m_contents_map";
	
	//메타 키
//	public static String vod_meta_key = "vod_meta_key";
//	public static String people_meta_key = "people_meta_key";
	
	//메뉴 그리드 버전 
//	public static String grid_version_key = "menu_grid_version";
	
	private Map<String, String> servicePackNo;
	public boolean isServicePackNo(String key) {
//		return servicePackNo.containsKey(key);
		boolean result = false;
		if( servicePackNo != null){
			result = servicePackNo.containsKey(key);
		}
		return result;
	}

//	public boolean isUseTmp() {
//		return useTmp;
//	}
//
//	public void setUseTmp(boolean useTmp) {
//		this.useTmp = useTmp;
//	}

//	public Map<String, Object> getHOME_MENU_STATE() {
//		if(HOME_MENU_STATE == null){
//			HOME_MENU_STATE = new HashMap<String, Object>();
//			HOME_MENU_STATE.put("home_state", "N") ;
//		}
//		return new HashMap<String, Object>(HOME_MENU_STATE);
//	}
//
//	public void setHOME_MENU_STATE(Boolean home_menu) {
//		
//		if(HOME_MENU_STATE == null) {
//			HOME_MENU_STATE = new HashMap<String, Object>();
//		}
//		
//		if(home_menu) {
//			HOME_MENU_STATE.put("home_state", "Y") ;
//		} else {
//			HOME_MENU_STATE.put("home_state", "N");
//		}
//	}
	
//	public Boolean getBtvRecState(String ui_name) {
//		Boolean state = (Boolean) redisClient.hget(XpgCommon.BTV_REC_STATE, ui_name);
//		if(state != null){
//			return state; 
//		}else{
//			redisClient.hset(XpgCommon.BTV_REC_STATE, ui_name, false);
//			return false;
//		}
//	}
//	
//	public void setBtvRecState(Boolean value, String ui_name) {
//		redisClient.hset(XpgCommon.BTV_REC_STATE, ui_name, value);
//	}
	
//	public String getPeopleRefUrl(){
//		return peopleRefUrl;
//	}
	
//	public String getImageUrl(){
//		return imageUrl;
//	}
	
//	public String getDate() {
//		Date now = new Date();
//		SimpleDateFormat nDate = new SimpleDateFormat("yyyyMMdd");
//		return nDate.format(now);
//	}
	// hso 2015.01.27 Calendar로 계산하여 리턴하는 함수 추가.
//	public String getCuDate(int day){
//		Locale currLocale = new Locale("KOREAN","KOREA");
//		String pattern = "yyyyMMdd";
//		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);
//		
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.DATE, day);
//        return formatter.format(cal.getTime());
//	}
	
//	public String getDateWithMilliSecond() {  // 20160824 BBH - request/response time 기록용
//		long time = System.currentTimeMillis(); 
//		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
//		return dayTime.format(new Date(time));		
//	}
	
/*	public String getCurMGRedisIP() {
		if(curMGRedisIP == null) {
			curMGRedisIP = StringUtils.defaultIfEmpty(redisClient.getMainMgRedisIp(), DEFAULT_MAIN_MG_REDIS_IP);
		}
		return curMGRedisIP;
	}*/

//	public void setCurMGRedisIP(String ip) {
//		curMGRedisIP = ip;
//	}

/*	public void changeActiveRedisMG(String requestIP) {
		// MG의 Main과 Sub의 IP 체크	(양정우M 요청)					by oh2jj - 2015.04.02
		//Boolean ynMgFlag = checkMgFlag(requestIP);
		
		//if( ynMgFlag == true) {
		//	setCurMGRedisIP(requestIP);
		//	redisClient.changeActiveRedisMG(requestIP); // jwban 0106
		//}
	}
	*/
	// MG IP Check
//	public Boolean checkMgFlag(String requestIP) {
//		Boolean flag 	= false;
//		String mainMgIp = StringUtils.defaultIfEmpty(get("managed.mg.server"), "");		// Main MG IP
//		String subMgIp 	= StringUtils.defaultIfEmpty(get("managed.mg.server.sub"), "");	// Sub MG IP
//		if(mainMgIp.equals(requestIP)) {
//			flag = true;
//		}else if (subMgIp.equals(requestIP)) {
//			flag = true;
//		}
//		
//		return flag;
//	}
	
	
	
	public void setUseRedisCluster(boolean value){
		bUseRedisCluster = value;
	}
	
	public boolean getUseRedisCluster(){
		return bUseRedisCluster;
	}
	
	
	//현재 Host IP에 맞는 MG Redis Server IP리턴
//	public String findMGRedisAddr(String hostIp){
//		if(hostIp == null || "".equals(hostIp)){
//			return null;
//		}
//		
//		//1.성수 Service Server 인지 check
//		if(isSameServer(hostIp , getServiceServerList("server.seo.svc.url"))){
//			return get("managed.mg.server");
//		}
//		
//		//2.양재 Service Server인지 check
//		if(isSameServer(hostIp , getServiceServerList("server.yang.svc.url"))){
//			return get("managed.mg.server.sub");
//		}
//		
//		
//		return get("managed.mg.server");
//	}
	
//	private String[] getServiceServerList(String prop){
//		if(prop == null || "".equals(prop)){
//			return null;
//		}
//		
//		String serverStr = get(prop);
//
//		return serverStr.split("\\|");
//	}
	
	
//	private boolean isSameServer(String hostIp , String[] list){
//		if(hostIp == null || "".equals(hostIp) || list == null || list.length == 0){
//			return false;
//		}
//		
//		boolean isExist = false;
//		for (String aList : list) {
//			if (hostIp.equals(aList)) {
//				isExist = true;
//				break;
//			}
//		}
//
//		return isExist;
//	}
	
	
	/*
	 * BtvPlus Menu 필터링 관련
	 */
	// 스마트리모콘의 경우 2단메뉴 이하에서는 d_type(display_type)이 0, 5, 40, 45 인 메뉴만 보여준다. 
//	public final static List<String> CONTAIN_D_TYPE_FOR_BTVPLUS = Arrays.asList("0", "5", "40", "45");

	// TV다시보기 > 월정액가입(22252), Btv추천 > 8월최신영화(23039) 필터링, 사용자정보(1), 지로원 청구서(36166, 36168)
	// IPTV2.0팁 메뉴도 제외(28621) 표팀장님요청				by oh2jj - 16.05.26
//	public final static List<String> FILTER_MENU_ID_FOR_BTVPLUS = Arrays.asList("22252", "23039", "36166", "36168", "1", "28621");
}
