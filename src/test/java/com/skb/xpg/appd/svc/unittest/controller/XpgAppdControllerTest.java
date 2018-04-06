package com.skb.xpg.appd.svc.unittest.controller;

import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.skb.xpg.appd.svc.BaseControllerTest;

public class XpgAppdControllerTest extends BaseControllerTest {
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	
	@Before
	public void setUp() throws Exception {
		
	}

	// 1. APPD-001 (홈배너) 성공 
	@Test  
	public void getHomeBanner_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/home/banner?IF=IF-XPG-APPD-001&ver=v1&response_format=json&id_package=20&menu_id=A000003145&stb_id=%7BF2992BBB-E4EB-11E5-A490-FDCFBFF8EC17%7D&ui_name=BSUHD2&prom_resltn_cd=185x128";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 1. APPD-001 (홈배너) 실패 
	@Test 
	public void getHomeBanner_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/home/banner?IF=IF-XPG-APPD-001&ver=v1&response_format=json&id_package=20&menu_id=A000003145&stb_id=%7BF2992BBB-E4EB-11E5-A490-FDCFBFF8EC17%7D&prom_resltn_cd=185x128";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	// 2. APPD-002 (전체메뉴 배너) 성공 
	@Test  
	public void getAllmenuBanner_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/allmenu/banner?IF=IF-XPG-APPD-002&ver=v1&response_format=json&ui_name=BSUHD2&service_code=0&id_package=15&menu_id=A000001712&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&version=0&prom_top_cd=1x1&prom_bottom_cd=1x1";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 2. APPD-002 (전체메뉴 배너) 실패 
	@Test 
	public void getAllmenuBanner_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/allmenu/banner?IF=IF-XPG-APPD-002&ver=v1&response_format=json&service_code=0&id_package=15&menu_id=A000001712&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&version=0&prom_top_cd=1x1&prom_bottom_cd=1x1";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	// 3. APPD-003 (컨텐츠 별 평점 및 부가정보) 성공 
	@Test  
	public void getContentPntEpiInfo_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/content/pntEpiInfo?IF=IF-XPG-APPD-003&ver=v1&response_format=json&ui_name=BSUHD2&id_package=15&con_id=%7BA683170E-7FD1-11E6-B970-61A99837AD11%7D&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&sw_ver=0.1.163&stb_model=SMT-E5030&st_cd=224x224&st_b_cd=404x227&pre_cd=96x72&pre_b_cd=404x227&hero_cd=960x540&hash_id=aa&epi_resltn_cd=252X142&epi_scr_resltn_cd=282x159&cmt_cd=1x1&cmt_b_cd=2x2";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 3. APPD-003 (컨텐츠 별 평점 및 부가정보) 실패 
	@Test 
	public void getContentPntEpiInfo_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/content/pntEpiInfo?IF=IF-XPG-APPD-003&ver=v1&response_format=json&id_package=15&con_id=%7BA683170E-7FD1-11E6-B970-61A99837AD11%7D&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&sw_ver=0.1.163&stb_model=SMT-E5030&st_cd=224x224&st_b_cd=404x227&pre_cd=96x72&pre_b_cd=404x227&hero_cd=960x540&hash_id=aa&epi_resltn_cd=252X142&epi_scr_resltn_cd=282x159&cmt_cd=1x1&cmt_b_cd=2x2";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// 4. APPD-004 (연관컨텐츠) 성공 
	@Test  
	public void getContentRefInfo_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/content/refInfo?IF=IF-XPG-APPD-004&ver=v1&response_format=json&ui_name=BSUHD2&id_package=20&con_id=%7B0058813D-1C47-11DF-9CC2-2D5B8C97D6D1%7D&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&sw_ver=3.3.144&stb_model=TD910H&link_resltn_cd=180x258&rst_type=hd";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 4. APPD-004 (연관컨텐츠) 실패 
	@Test 
	public void getContentRefInfo_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/content/refInfo?IF=IF-XPG-APPD-004&ver=v1&response_format=json&id_package=20&con_id=%7B0058813D-1C47-11DF-9CC2-2D5B8C97D6D1%7D&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&sw_ver=3.3.144&stb_model=TD910H&link_resltn_cd=180x258&rst_type=hd";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	// 5. APPD-005 (인물정보) 성공 
	@Test  
	public void getContentStaffInfoList_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/content/staffInfoList?IF=IF-XPG-APPD-005&ver=v1&response_format=json&ui_name=BSUHD2&id_package=20&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&id=MP0000000549&resltn_cd=200x200";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 5. APPD-005 (인물정보) 실패 
	@Test 
	public void getContentStaffInfoList_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		// ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/content/staffInfoList?IF=IF-XPG-APPD-005&ver=v1&response_format=json&id_package=20&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&id=MP0000000549&resltn_cd=200x200";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// 6. APPD-006 (실시간 채널순위) 성공 
	@Test  
	public void getEpgChannel_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/epg/channel?IF=IF-XPG-APPD-006&ver=v1&response_format=json&ui_name=BSUHD2&id_package=20&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&region_code=mbc=1^kbs=41^sbs=61^uhdmbc=100^uhdkbs=100^UMAX=100";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 6. APPD-006 (실시간 채널순위) 실패 
	@Test 
	public void getEpgChannel_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/epg/channel?IF=IF-XPG-APPD-006&ver=v1&response_format=json&id_package=20&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&region_code=mbc=1^kbs=41^sbs=61^uhdmbc=100^uhdkbs=100^UMAX=100";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	// 7. APPD-007 (실시간 채널순위) 성공 
	@Test  
	public void getRealTimeChannel_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/realTime/channel?IF=IF-XPG-APPD-007&ver=v1&response_format=json&ui_name=BSUHD2&id_package=20";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 7. APPD-007 (실시간 채널순위) 실패 
	@Test 
	public void getRealTimeChannel_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/realTime/channel?IF=IF-XPG-APPD-007&ver=v1&response_format=json&id_package=20";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	// 8. APPD-008 (인기VOD정보) 성공 
	@Test  
	public void getPopularVodInfo_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/popular/vodInfo?IF=IF-XPG-APPD-008&ver=v1&response_format=json&ui_name=BSUHD2&id_package=20&rst_type=4k&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 8. APPD-008 (인기VOD정보) 실패 
	@Test 
	public void getPopularVodInfo_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		// ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/popular/vodInfo?IF=IF-XPG-APPD-008&ver=v1&response_format=json&id_package=20&rst_type=4k&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// 9. APPD-009 (검색지정컨텐츠) 성공 
	@Test  
	public void getContentSpecify_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/content/specify?IF=IF-XPG-APPD-009&response_format=json&id_package=20&ui_name=BSUHD2&ver=v1&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&ui_version=4301-03-03&resltn_cd=200x200";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 9. APPD-009 (검색지정컨텐츠) 실패 
	@Test 
	public void getContentSpecify_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/content/specify?IF=IF-XPG-APPD-009&response_format=json&id_package=20&ver=v1&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&ui_version=4301-03-03&resltn_cd=200x200";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// 10. APPD-010 (코너별 모아보기) 성공 
	@Test  
	public void getContentCorner_normal_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String getSynopsisSimpleRequestData ="/v1/content/corner?IF=IF-XPG-APPD-010&response_format=json&id_package=20&ui_name=BSUHD2&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&epi_gid=CN0000006379";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod. GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 10. APPD-010 (코너별 모아보기) 실패 
	@Test 
	public void getContentCorner_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//ui_name 값 없슴		
	    String getSynopsisSimpleRequestData ="/v1/content/corner?IF=IF-XPG-APPD-010&response_format=json&id_package=20&stb_id=%7BE9CA4B0C-714E-11DE-A053-B5248267E59C%7D&epi_gid=CN0000006379";
		String expected = "MP-9001";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크		
		if(null != response){
			System.out.println(response);
		}
		try {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
