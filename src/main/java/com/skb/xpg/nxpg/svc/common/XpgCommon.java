package com.skb.xpg.nxpg.svc.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class XpgCommon {
/*
 * 	홈GNB I/F
	전체메뉴 I/F
	프로모션 I/F
	블록 I/F
	블록-그리드 I/F
	이벤트 I/F
	월정액정보 I/F
	CW연동그리드 I/F
	시놉 I/F
	상품 I/F
	구매 I/F
	CW관련 콘텐츠 I/F
	EPG 정보 I/F
	채널플러스 I/F
	키즈그리드 I/F
	CW 그리드 I/F
	시놉 I/F
	캐릭터메뉴 I/F
	키즈GNB I/F
	캐릭터정보 I/F
	월정액 I/F
	장르메뉴 I/F
 * */
	
	public static final String SYNOPSIS = "synopsis";
	public static final String META = "meta";
	public static final String META_PEOPLE = "metaPeople";
	
	public static final String GRID_RELATED_CW = "metaPeople";
	public static final String GRID_RECOMMEND_CW = "cwGrid";
	
	public static final String MENU_ALL = "menuAll";
	public static final String MENU_HOME = "menuHome";
	public static final String MENU_MONTH = "menuMonth";

	public static final String BLOCK = "block";
	public static final String BLOCK_GRID = "blockGrid";

	public static final String PROMOTION_BANNER = "promotionBanner";
	public static final String PROMOTION_EVENT = "promotionEvent";
	
	public static final String PRODUCT = "product";
	public static final String PRODUCT_BUY = "productBuy";
	
	public static final String EPG_CHANNEL = "epgChannel";
	public static final String CHANNEL_PLUS = "channelPlus";
}
