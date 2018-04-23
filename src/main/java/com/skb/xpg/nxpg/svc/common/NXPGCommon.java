package com.skb.xpg.nxpg.svc.common;

public class NXPGCommon {

	public static final String MENU_GNB = "menu_gnb";
	public static final String MENU_ALL = "menu_all";
	public static final String BLOCK_BLOCK = "block_block";
	public static final String GRID_BANNER = "grid_banner";
	public static final String GRID_CONTENTS = "grid_contents";
	public static final String BIG_BANNER = "big_banner";
	public static final String BLOCK_MONTH = "block_month";
	public static final String SYNOPSIS_CONTENTS = "synopsis_contents";
	public static final String SYNOPSIS_SRISINFO = "synopsis_srisInfo";
	public static final String SYNOPSIS_COMMERCE  = "synopsis_commerce";
	public static final String SYNOPSIS_GATEWAY  = "synopsis_gateway";
	public static final String SYNOPSIS_SRIS  = "synopsis_sris";
	public static final String SYNOPSIS_LIVECHILDSTORY  = "synopsis_liveChildStory";
	public static final String CONTENTS_SYNOPSIS = "contents_synopsis";
	public static final String CONTENTS_PEOPLE = "contents_people";
	public static final String CONTENTS_GWSYNOP = "contents_gwsynop";
	public static final String CONTENTS_PURCHARES  = "contents_purchares";
	public static final String CONTENTS_PHRASE  = "contents_phrase";
	public static final String CONTENTS_COMMERCE  = "contents_commerce";
	public static final String CONTENTS_CORNER  = "contents_corner";
	public static final String CONTENTS_CIDINFO  = "contents_cidinfo";
	public static final String CONTENTS_VODLIST  = "contents_vodlist";
	public static final String CONTENTS_RATING  = "contents_rating";
	public static final String CONTENTS_LFTSYNOP  = "contents_lftsynop";
	public static final String CONTENTS_PREVIEW  = "contents_preview";
	public static final String CONTENTS_GATHER  = "corner_gather";
	public static final String CONTENTS_REVIEW  = "contents_review";
	public static final String CHANNEL_RATING  = "channel_rating";
	public static final String EPG_INFO  = "epg_info";
	public static final String GENRE_INFO  = "genre_info";
	public static final String PEOPLE_INFO  = "people_info";
	public static final String MENU_KIDSGNB  = "menu_kidsGnb";
	public static final String MENU_KIDSCHARACTER  = "menu_kidsCharacter";
	public static final String GRID_CONTENTS_ITEM  = "grid_contents_item";
	public static final String VERSION  = "version";

	public static boolean useFirstRedis = true;
	
	public static int redisErrorCount = 0;

	public static boolean isUseFirstRedis() {
		return useFirstRedis;
	}

	public static void switchUseRedis() {
		NXPGCommon.useFirstRedis = !NXPGCommon.useFirstRedis;
	}
	
	public static int getRedisErrorCount() {
		return redisErrorCount;
	}

	public static void setRedisErrorCount(int redisErrorCount) {
		NXPGCommon.redisErrorCount = redisErrorCount;
	}

	public static void initRedisErrorCount() {
		NXPGCommon.redisErrorCount = 0;
	}
	
}
