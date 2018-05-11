package com.skb.xpg.nxpg.svc.unittest.controller;

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

import com.skb.xpg.nxpg.svc.BaseControllerTest;

public class ControllerTest extends BaseControllerTest {
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	
	@Before
	public void setUp() throws Exception {
		
	}

	// 1. IF-NXPG-001 성공 
	@Test  
	public void get001_success() throws JSONException {
		
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
			
		//요청값 과 예상 결과갑 설정
	    String RequestData = "";
		String expected = "0000";
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData),
																HttpMethod.GET, entity, String.class);
		//호출 결과 및 정합성 체크
		if(null != response){
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	// 2. IF-NXPG-001 실패 
	@Test 
	public void get001_abnormal_success() throws JSONException {
		//Header Setting
		headers.add("Content-Type","application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");	
		
		//요청값 과 예상 결과갑 설정
		//id_package 값 없슴		
		String RequestData ="";
		String expected = "9998";
		
		//대상 uri 호출
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData),
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
