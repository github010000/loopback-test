package com.skb.xpg.appd.svc.unittest.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skb.xpg.appd.svc.BaseServiceTest;
import com.skb.xpg.appd.svc.svc.XpgAppdService;

public class XpgAppdServiceTest extends BaseServiceTest {
	
	@Autowired
	private XpgAppdService xpgAppdService;
	
	@Before
	public void setUp() throws Exception {

	}
	
	// 1. APPD-001 (홈배너) 성공
	@Ignore
	@Test	
	public void getHomeMenuBanner_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();
			
		param.put("ui_name", "BSUHD2");
		param.put("menu_id", "A000003145");
		param.put("prom_resltn_cd",	"185x128");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getHomeMenuBanner(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 1. APPD-001 (홈배너) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getHomeMenuBanner_abnormal_success() {
		// 입력 값 설정, 조회 값 stb_id 제거
		Map<String, String> param = new HashMap<String, String>();

		// param.put("ui_name", "BSUHD2");
		param.put("menu_id", "A000003145");
		param.put("prom_resltn_cd",	"185x128");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getHomeMenuBanner(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}
	
	// 2. APPD-002 (전체메뉴 배너) 성공 
	@Ignore
	@Test	
	public void getPromoMenu_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("ui_name", "BSUHD2");
		param.put("service_code", "0");
		param.put("id_package", "15");
		param.put("prom_top_cd", "1x1");
		param.put("prom_bottom_cd", "1x1");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getPromoMenu(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 2. APPD-002 (전체메뉴 배너) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getPromoMenu_abnormal_success() {
		// 입력 값 설정, 조회 값 ui_name 제거
		Map<String, String> param = new HashMap<String, String>();

//		param.put("ui_name", "BSUHD2");
		param.put("service_code", "0");
		param.put("id_package", "15");
		param.put("prom_top_cd", "1x1");
		param.put("prom_bottom_cd", "1x1");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getPromoMenu(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}

	// 3. APPD-003 (컨텐츠 별 평점 및 부가정보) 성공 
	@Ignore
	@Test	
	public void getContentPntEpiInfo_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("content_id", "%7BA683170E-7FD1-11E6-B970-61A99837AD11%7D");
		param.put("st_cd", "224x224");
		param.put("st_b_cd", "404x227");
		param.put("hero_cd", "960x540");
		param.put("epi_resltn_cd", "252X142");
		param.put("epi_scr_resltn_cd", "282x159");
		param.put("cmt_cd", "1x1");
		param.put("cmt_b_cd", "2x2");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getContentPntEpiInfo(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 3. APPD-003 (컨텐츠 별 평점 및 부가정보) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getContentPntEpiInfo_abnormal_success() {
		// 입력 값 설정, 조회 값 content_id 제거
		Map<String, String> param = new HashMap<String, String>();

//		param.put("content_id", "%7BA683170E-7FD1-11E6-B970-61A99837AD11%7D");
		param.put("st_cd", "224x224");
		param.put("st_b_cd", "404x227");
		param.put("hero_cd", "960x540");
		param.put("epi_resltn_cd", "252X142");
		param.put("epi_scr_resltn_cd", "282x159");
		param.put("cmt_cd", "1x1");
		param.put("cmt_b_cd", "2x2");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getContentPntEpiInfo(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}

	// 4. APPD-004 (연관컨텐츠) 성공 
	@Ignore
	@Test	
	public void getContentRefInfo_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("contentId", "%7B0058813D-1C47-11DF-9CC2-2D5B8C97D6D1%7D");
		param.put("link_resltn_cd", "180x258"); // 관련컨텐츠 포스터 해상도
		param.put("rst_type", "hd"); // 해상도 구분
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getContentRefInfo(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 4. APPD-004 (연관컨텐츠) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getContentRefInfo_abnormal_success() {
		// 입력 값 설정, 조회 값 stb_id 제거
		Map<String, String> param = new HashMap<String, String>();

//		param.put("contentId", "%7B0058813D-1C47-11DF-9CC2-2D5B8C97D6D1%7D");
		param.put("link_resltn_cd", "180x258"); // 관련컨텐츠 포스터 해상도
		param.put("rst_type", "hd"); // 해상도 구분
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getContentRefInfo(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}

	// 5. APPD-005 (인물정보) 성공 
	@Ignore
	@Test	
	public void getStaffInfoList_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("id", "MP0000000549");
		param.put("packageId", "20");
		param.put("resltn_cd", "200x200");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getStaffInfoList(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 5. APPD-005 (Epg 채널정보) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getStaffInfoList_abnormal_success() {
		// 입력 값 설정, 조회 값 id 제거
		Map<String, String> param = new HashMap<String, String>();

//		param.put("id", "MP0000000549");
		param.put("packageId", "20");
		param.put("resltn_cd", "200x200");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getStaffInfoList(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}


	// 6. APPD-006 (Epg 채널정보) 성공 
	@Ignore
	@Test	
	public void getgetEpgChannel_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("region", "mbc=1^kbs=41^sbs=61^uhdmbc=100^uhdkbs=100^UMAX=100");
		
//		result = stbService.getEpgCategoryMappingInfo(param);
		Map<String, Object> returnVal = xpgAppdService.getEpgChannel(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 6. APPD-006 (인물정보) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getgetEpgChannel_abnormal_success() {
		// 입력 값 설정, 조회 값 id 제거
		Map<String, String> param = new HashMap<String, String>();

//		param.put("region", "mbc=1^kbs=41^sbs=61^uhdmbc=100^uhdkbs=100^UMAX=100");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getEpgChannel(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}
	
	// 7. APPD-007 (실시간 채널순위) 성공 
	@Ignore
	@Test	
	public void getRealTimeChannel_normal_success() {
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getRealTimeChannel();
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}		
    }

	// 8. APPD-008 (인기VOD정보) 성공 
	@Ignore
	@Test	
	public void getGenreRankVodAndRefVod_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("resltn_cd", "");
		param.put("category_id", "");
		param.put("con_id", "");
		param.put("item", "");
		param.put("id_package", "20");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getGenreRankVodAndRefVod(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 8. APPD-008 (인기VOD정보) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getGenreRankVodAndRefVod_abnormal_success() {
		// 입력 값 설정, 조회 값 stb_id 제거
		Map<String, String> param = new HashMap<String, String>();

		param.put("resltn_cd", "");
		param.put("category_id", "");
		param.put("con_id", "");
		param.put("item", "");
//		param.put("id_package", "20");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getGenreRankVodAndRefVod(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}

	// 9. APPD-009 (검색지정컨텐츠) 성공 
	@Ignore
	@Test	
	public void getSpecifyContents_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("id_package", "20");
		param.put("resltn_cd", "200x200");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getSpecifyContents(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 9. APPD-009 (검색지정컨텐츠) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getSpecifyContents_abnormal_success() {
		// 입력 값 설정, 조회 값 stb_id 제거
		Map<String, String> param = new HashMap<String, String>();

//		param.put("id_package", "20");
		param.put("resltn_cd", "200x200");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getSpecifyContents(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}

	// 10. APPD-010 (코너별 모아보기) 성공 
	@Ignore
	@Test	
	public void getContentsCorner_normal_success() {
		// 입력 값 설정
		Map<String, String> param = new HashMap<String, String>();

		param.put("id_package", "20");
		param.put("resltn_cd", "");
		param.put("epi_gid", "CN0000006379");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getCorner(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("OK")) {
			System.out.println("OK");
		}
    }

	// 10. APPD-010 (코너별 모아보기) 실패 
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getContentsCorner_abnormal_success() {
		// 입력 값 설정, 조회 값 stb_id 제거
		Map<String, String> param = new HashMap<String, String>();

		param.put("id_package", "20");
		param.put("resltn_cd", "");
//		param.put("epi_gid", "CN0000006379");
		
		// 서비스 호출
		Map<String, Object> returnVal = xpgAppdService.getCorner(param);
		
		// 호출 결과 및 정합성 체크
		if(returnVal != null){
			System.out.println(returnVal);
		}
		if (returnVal.get("result").toString().equals("MP-9001")) {
			System.out.println("MP-9001");
		}
	}
	
}
