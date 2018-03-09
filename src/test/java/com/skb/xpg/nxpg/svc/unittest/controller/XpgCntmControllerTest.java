//package com.skb.xpg.nxpg.svc.unittest.controller;
//
//import static org.junit.Assert.assertEquals;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//
//import com.skb.xpg.nxpg.svc.BaseControllerTest;
//
//public class XpgCntmControllerTest extends BaseControllerTest {
//	
//	TestRestTemplate restTemplate = new TestRestTemplate();
//	HttpHeaders headers = new HttpHeaders();
//
//	
//	@Before
//	public void setUp() throws Exception {
//		
//	}
//
//	// 1. CNTM-001 (일반 시놉시스) 성공 
//	@Test  
//	public void getSynopsisNormal_normal_success() throws JSONException {
//		
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//			
//		//요청값 과 예상 결과갑 설정
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/normal?IF=IF-XPG-CNTM-001&response_format=json&ui_name=BSUHD2&service_code=0&id_package=20&cur_menu=108463&stb_id=%7B5FA8A750-FD44-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=0.1.163&stb_model=SMT-E5030&sub_pack=25&iptv_pack=1&resltn_cd=144x206&pur_resltn_cd=144x206&pre_cd=96x72&pre_b_cd=404x227&rst_type=4k&yn_recent=N&g_gubun=30&g_code=1662&hash_id&track_id&con_id=%7BA2C46D9E-1B04-11E6-B49C-5702654CECC8%7D";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod. GET, entity, String.class);
//		//호출 결과 및 정합성 체크
//		if(null != response){
//			System.out.println(response);
//		}
//		assertEquals(200, response.getStatusCodeValue());
//	}
//	
//	// 1. CNTM-001 (일반 시놉시스) 실패 
//	@Test 
//	public void getSynopsisNormal_abnormal_success() throws JSONException {
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//		
//		//요청값 과 예상 결과갑 설정
//		//id_package 값 없슴		
//		String getSynopsisSimpleRequestData ="/v1/synopsis/normal?IF=IF-XPG-CNTM-001&response_format=json&service_code=0&id_package=20&cur_menu=108463&stb_id=%7B5FA8A750-FD44-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=0.1.163&stb_model=SMT-E5030&sub_pack=25&iptv_pack=1&resltn_cd=144x206&pur_resltn_cd=144x206&pre_cd=96x72&pre_b_cd=404x227&rst_type=4k&yn_recent=N&g_gubun=30&g_code=1662&hash_id&track_id";
//		String expected = "MP-9001";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod.GET, entity, String.class);
//		//호출 결과 및 정합성 체크		
//		if(null != response){
//			System.out.println(response);
//		}
//		try {
//			JSONObject obj = new JSONObject(response.getBody());
//			if (expected.equals(obj.getString("result")) == false) {
//				System.out.println("Fail");
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//	
//	// 2. CNTM-002 (통합시놉시스) 성공
//	@Test  
//	public void getSynopsisIntegration_normal_success() throws JSONException {
//		
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//			
//		//요청값 과 예상 결과갑 설정
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/integration?IF=IF-XPG-CNTM-002&response_format=json&ui_name=BSUHD2&service_code=0&id_package=20&cur_menu=108773&stb_id=%7BF93D4E1F-E4EB-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=4.1.487&stb_model=BKO-100&sub_pack=25&iptv_pack=1&resltn_cd=144x206&pur_resltn_cd=144x206&pre_cd=96x72&pre_b_cd=404x227&hero_cd=750x422&rst_type=hd&yn_recent=N&g_gubun=30&g_code=1662&hash_id&track_id&con_id=%7BA2C46D9E-1B04-11E6-B49C-5702654CECC8%7D";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod. GET, entity, String.class);
//		//호출 결과 및 정합성 체크
//		if(null != response){
//			System.out.println(response);
//		}
//		assertEquals(200, response.getStatusCodeValue());
//	}
//	
//	// 2. CNTM-002 (통합시놉시스) 실패  
//	@Test 
//	public void getSynopsisIntegration_abnormal_success() throws JSONException {
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//		
//		//요청값 과 예상 결과갑 설정
//		//cond_id 값 없슴		
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/integration?IF=IF-XPG-CNTM-002&response_format=json&service_code=0&id_package=20&cur_menu=108773&stb_id=%7BF93D4E1F-E4EB-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=4.1.487&stb_model=BKO-100&sub_pack=25&iptv_pack=1&resltn_cd=144x206&pur_resltn_cd=144x206&pre_cd=96x72&pre_b_cd=404x227&hero_cd=750x422&rst_type=hd&yn_recent=N&g_gubun=30&g_code=1662&hash_id&track_id";
//		String expected = "MP-9001";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod.GET, entity, String.class);
//		//호출 결과 및 정합성 체크		
//		if(null != response){
//			System.out.println(response);
//		}
//		try {
//			JSONObject obj = new JSONObject(response.getBody());
//			if (expected.equals(obj.getString("result")) == false) {
//				System.out.println("Fail");
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//
//	// 3. CNTM-003 (간편시놉시스) 성공
//	@Test  
//	public void getSynopsisSimple_normal_success() throws JSONException {
//		
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//			
//		//요청값 과 예상 결과갑 설정
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/simple?IF=IF-XPG-CNTM-003&response_format=json&ui_name=BSUHD2&service_code=0&id_package=20&stb_id=%7BF93D4E1F-E4EB-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=4.1.487&stb_model=BKO-100&resltn_cd=144x206&yn_recent=N&hash_id&con_id=%7BA2C46D9E-1B04-11E6-B49C-5702654CECC8%7D";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod. GET, entity, String.class);
//		//호출 결과 및 정합성 체크
//		if(null != response){
//			System.out.println(response);
//		}
//		assertEquals(200, response.getStatusCodeValue());
//	}
//	
//	// 3. CNTM-003 (간편시놉시스) 실패  
//	@Test 
//	public void getSynopsisSimple_abnormal_success() throws JSONException {
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//		
//		//요청값 과 예상 결과갑 설정
//		//stb_id 값 없슴		
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/simple?IF=IF-XPG-CNTM-003&response_format=json&service_code=0&id_package=20&stb_id=%7BF93D4E1F-E4EB-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=4.1.487&stb_model=BKO-100&resltn_cd=144x206&yn_recent=N&hash_id";
//		String expected = "MP-9001";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod.GET, entity, String.class);
//		//호출 결과 및 정합성 체크		
//		if(null != response){
//			System.out.println(response);
//		}
//		try {
//			JSONObject obj = new JSONObject(response.getBody());
//			if (expected.equals(obj.getString("result")) == false) {
//				System.out.println("Fail");
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//
//	// 4. CNTM-004 (스마트 편성표) 성공
//	@Test  
//	public void getSynopsisSmart_normal_success() throws JSONException {
//		
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//			
//		//요청값 과 예상 결과갑 설정
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/smart?IF=IF-XPG-CNTM-004&response_format=json&ui_name=BSUHD2&service_code=0&id_package=20&cur_menu=108773&stb_id=%7BF93D4E1F-E4EB-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=4.1.487&stb_model=BKO-100&sub_pack=25&iptv_pack=1&resltn_cd=144x206&pur_resltn_cd=144x206&rst_type=hd&yn_recent=N&g_gubun=30&g_code=1662&hash_id&con_id=%7BA2C46D9E-1B04-11E6-B49C-5702654CECC8%7D";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod. GET, entity, String.class);
//		//호출 결과 및 정합성 체크
//		if(null != response){
//			System.out.println(response);
//		}
//		assertEquals(200, response.getStatusCodeValue());
//	}
//	
//	// 4. CNTM-004 (스마트 편성표) 실패  
//	@Test 
//	public void getSynopsisSmart_abnormal_success() throws JSONException {
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//		
//		//요청값 과 예상 결과갑 설정
//		//cond_id 값 없슴		
//	    String getSynopsisSimpleRequestData ="/v1/synopsis/smart?IF=IF-XPG-CNTM-004&response_format=json&service_code=0&id_package=20&cur_menu=108773&stb_id=%7BF93D4E1F-E4EB-11E5-A490-FDCFBFF8EC17%7D&ver=v1&sw_ver=4.1.487&stb_model=BKO-100&sub_pack=25&iptv_pack=1&resltn_cd=144x206&pur_resltn_cd=144x206&rst_type=hd&yn_recent=N&g_gubun=30&g_code=1662&hash_id";
//		String expected = "MP-9001";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod.GET, entity, String.class);
//		//호출 결과 및 정합성 체크		
//		if(null != response){
//			System.out.println(response);
//		}
//		try {
//			JSONObject obj = new JSONObject(response.getBody());
//			if (expected.equals(obj.getString("result")) == false) {
//				System.out.println("Fail");
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//
//	// 5. CNTM-005 (통합 컨텐츠 리스트) 성공
//	@Test  
//	public void getContentList_normal_success() throws JSONException {
//		
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//			
//		//요청값 과 예상 결과갑 설정
//	    String getSynopsisSimpleRequestData ="/v1/content/list?IF=IF-XPG-CNTM-005&response_format=json&id_package=20&g_gubun=30&g_code=1662&cur_menu=108773&stb_id=%7BF481E3A7-DCC3-11DE-ACF0-AB20203CAC17%7D&sw_ver=3.3.144&stb_model=TD910H&iptv_pack&ui_name=BSUHD2&con_id=%7BA2C46D9E-1B04-11E6-B49C-5702654CECC8%7D&mst_id=%7BD3E94229-1B03-11E6-B49C-5702654CECC8%7D";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod. GET, entity, String.class);
//		//호출 결과 및 정합성 체크
//		if(null != response){
//			System.out.println(response);
//		}
//		assertEquals(200, response.getStatusCodeValue());
//	}
//	
//	// 5. CNTM-005 (통합 컨텐츠 리스트) 실패  
//	@Test 
//	public void getContentList_abnormal_success() throws JSONException {
//		//Header Setting
//		headers.add("Content-Type","application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");	
//		
//		//요청값 과 예상 결과갑 설정
//		//cond_id 값 없슴		
//		String getSynopsisSimpleRequestData ="/v1/content/list?IF=IF-XPG-CNTM-005&response_format=json&id_package=20&g_gubun=30&g_code=1662&cur_menu=108773&stb_id=%7BF481E3A7-DCC3-11DE-ACF0-AB20203CAC17%7D&sw_ver=3.3.144&stb_model=TD910H&iptv_pack";
//		String expected = "MP-9001";
//		
//		//대상 uri 호출
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(getSynopsisSimpleRequestData),
//																HttpMethod.GET, entity, String.class);
//		//호출 결과 및 정합성 체크		
//		if(null != response){
//			System.out.println(response);
//		}
//		try {
//			JSONObject obj = new JSONObject(response.getBody());
//			if (expected.equals(obj.getString("result")) == false) {
//				System.out.println("Fail");
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//}
