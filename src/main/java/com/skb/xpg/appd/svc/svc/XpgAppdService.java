package com.skb.xpg.appd.svc.svc;

import java.util.Map;

public interface XpgAppdService {
	
	/** 홈배너 (IF-XPG-APPD-001) */
	public Map<String, Object> getHomeMenuBanner(Map<String, String> param);
	
	/** 전체메뉴 배너 (IF-XPG-APPD-002) */
	public Map<String, Object> getPromoMenu(Map<String, String> param);
	
	/** 컨텐츠 별 평점 및 부가정보 (IF-XPG-APPD-003) */
	public Map<String, Object> getContentPntEpiInfo(Map<String, String> param);
	
	/** 연관컨텐츠 (IF-XPG-APPD-004) */
	public Map<String, Object> getContentRefInfo(Map<String, String> param);
	
	/** 인물정보 (IF-XPG-APPD-005) */
	public Map<String, Object> getStaffInfoList(Map<String, String> param);
	
	/** 실시간 채널순위 (IF-XPG-APPD-007) */
	public Map<String, Object> getEpgChannel(Map<String, String> param);
	
	/** 실시간 채널순위 (IF-XPG-APPD-007) */
	public Map<String, Object> getRealTimeChannel();
	
	/** 인기VOD정보 (IF-XPG-APPD-008) */
	public Map<String, Object> getGenreRankVodAndRefVod(Map<String, String> param);
	
	/** 검색지정 컨텐츠 (IF-XPG-APPD-009) */
	public Map<String, Object> getSpecifyContents(Map<String, String> param);

	/** 검색지정 컨텐츠 (IF-XPG-APPD-010) */
	public Map<String, Object> getCorner(Map<String, String> param);
}
