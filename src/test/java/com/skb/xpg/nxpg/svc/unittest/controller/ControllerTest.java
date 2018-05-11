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
		
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = "/task/adderror";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = null;
		for (int i = 0; i < 9; i++) {
			response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
					String.class);
		}
		if (null != response) {
			System.out.println(response);
		}
		
		RequestData = u001;
		response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
	}
	
	
	String u001 = "/v5/menu/gnb?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-001&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u002 = "/v5/menu/all?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-002&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u003 = "/v5/menu/block?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000000300&IF=IF-NXPG-003&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u005 = "/v5/menu/month?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018172&IF=IF-NXPG-005&prd_prc_id_lst=3240009&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u006 = "/v5/grid/grid?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018613&IF=IF-NXPG-006&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&page_no=1&page_cnt=20";
	String u007 = "/v5/grid/event?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000020226&IF=IF-NXPG-007&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u008 = "/v5/contents/rating?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-008&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&sris_id=CS01000249&page_no=1&page_cnt=2&site_cd=10";
	String u009 = "/v5/inter/cwgrid?menu_stb_svc_id=UI50_BSUHD&menu_id=NM1000018206&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&cw_call_id=TEST.NEW.APIS.PAGE&track_id=368.34727.1522911308391&session_id=b4566b6b-73b9-4f05-ab68-a80ff25a6c95&type=all&IF=IF-NXPG-009";
	String u010_1 = "/v5/contents/synopsis?menu_stb_svc_id=UI50_BSUHD2&search_type=1&epsd_id=CE0001293945&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_id=A000002200&yn_recent=Y&IF=IF-NXPG-010";
	String u010_2 = "/v5/contents/synopsis?menu_stb_svc_id=UI50_BSUHD2&search_type=2&epsd_rslu_id=%7B17E6CCA1-0A46-11E8-9252-FFA90356A7A9%7D&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&yn_recent=N&IF=IF-NXPG-010";
	String u011 = "/v5/people/info?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018172&prs_id=MP0000000005&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-011";
	String u012 = "/v5/inter/cwrelation?menu_stb_svc_id=UI50_BSUHD&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&cw_call_id=TEST.NEW.APIS.PAGE&track_id=368.34727.1522911308391&session_id=b4566b6b-73b9-4f05-ab68-a80ff25a6c95&type=all&epsd_id=CE0000005435&epsd_rslu_id=%7B8764D44C-1B04-11E6-B49C-5702654CECC8%7D&IF=IF-NXPG-012";
	String u013 = "/v5/added/epg?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018172&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-013";
	String u014 = "/v5/contents/gwsynop?menu_stb_svc_id=UI50_BSUHD2&sris_id=CS01152413&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-014";
	String u015 = "/v5/contents/commerce?menu_stb_svc_id=UI50_BSUHD2&sris_id=CS10007978&IF=IF-NXPG-015";
	String u016 = "/v5/contents/corner?menu_stb_svc_id=UI50_BSUHD2&cnr_grp_id=CN1000000157&IF=IF-NXPG-016";
	String u017 = "/v5/added/epggenre?menu_stb_svc_id=UI50_BSUHD2&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u018 = "/v5/added/realtimechannel?IF=IF-NXPG-018";
	String u101 = "/v5/menu/kzchar?menu_stb_svc_id=UI50_BSUHD2&order_type=01&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-101";
	String u102 = "/v5/menu/kzgnb?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-102&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";
	String u401 = "/v5/menu/lfthomemapping?menu_stb_svc_id=UI50_BSUHD2&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-401";
	String u403 = "/v5/contents/lftsynop?menu_stb_svc_id=UI50_BSUHD2&epsd_id=CE1000008260&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-403";


	@Test
	public void get001_normal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u001;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	public void get001_normal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u001;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get002_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u002;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get003_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u003;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get005_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u005;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get006_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u006;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get007_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u007;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get008_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u008;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get009_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u009;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get010_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u010_1;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get011_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u011;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get012_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u012;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get013_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u013;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get014_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u014;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get015_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u015;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get016_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u016;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get017_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u017;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get018_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u018;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get101_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u101;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get102_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u102;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get401_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u401;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void get403_normal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = u403;
		String expected = "0000";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
			JSONObject obj = new JSONObject(response.getBody());
			if (expected.equals(obj.getString("result")) == false) {
				System.out.println("Fail");
			}
			System.out.println(response);
		}
		assertEquals(200, response.getStatusCodeValue());
	}
	
	String au003_1 ="/v5/menu/block?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-003&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";	//9999 파라미터 누락
	String au003_2 ="/v5/menu/block?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM101110400&IF=IF-NXPG-003&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";	//9998 결과값 없음
	String au005_1 ="/v5/menu/month?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018172&IF=IF-NXPG-005&prd_prc_id_lst=1476793,1649234&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";	//9995 상품ID 올바르지 않음
	String au005_2 ="/v5/menu/month?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018172&IF=IF-NXPG-005&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";	//9999 파라미터 누락
	String au006_1 ="/v5/grid/grid?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1032018613&IF=IF-NXPG-006&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&page_no=1&page_cnt=20";	//9998 결과값 없음
	String au006_2 ="/v5/grid/grid?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-006&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&page_no=1&page_cnt=20";	//9999 파라미터 누락
	String au007_1 ="/v5/grid/event?menu_stb_svc_id=UI50_BSUHD2&menu_id=NM1000018808&IF=IF-NXPG-007&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";	//9998 결과값 없음
	String au007_2 ="/v5/grid/event?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-007&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D";	//9999 파라미터 누락
	String au008_1 ="/v5/contents/rating?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-008&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&sris_id=CS0292&page_no=1&page_cnt=2&site_cd=10";	//9998 결과값 없음
	String au008_2 ="/v5/contents/rating?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-008&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&page_no=1&page_cnt=2&site_cd=10";	//9999 파라미터 누락
	String au009_1 ="/v5/inter/cwgrid?menu_stb_svc_id=UI50_BSUHD&menu_id=NM1000018206&stb_id=%7B660D7F55-89D8-11E5-ADA814417%7D&cw_call_id=TEST.NEW.APIS.PAGE&track_id=368.34727.1522911308391&session_id=b4566b6b-73b9-4f05-ab68-a80ff25a6c95&type=all&IF=IF-NXPG-009";	//9998 결과값 없음
	String au009_2 ="/v5/inter/cwgrid?menu_stb_svc_id=UI50_BSUHD&menu_id=NM1000018206&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&track_id=368.34727.1522911308391&session_id=b4566b6b-73b9-4f05-ab68-a80ff25a6c95&type=all&IF=IF-NXPG-009";	//9999 파라미터 누락
	String au010_1 ="/v5/contents/synopsis?menu_stb_svc_id=UI50_BSUHD2&search_type=1&epsd_id=CE0001278822&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&yn_recent=Y&IF=IF-NXPG-010";	//9998 결과값 없음
	String au010_2 ="/v5/contents/synopsis?menu_stb_svc_id=UI50_BSUHD2&search_type=1&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&yn_recent=Y&IF=IF-NXPG-010";	//9999 타입확인
	String au010_3 ="/v5/contents/synopsis?menu_stb_svc_id=UI50_BSUHD2&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&yn_recent=Y&IF=IF-NXPG-010";	//9999 파라미터 누락
	String au011_1 ="/v5/people/info?menu_stb_svc_id=UI50_BSUHD2&prs_id=MP0000400005&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-011";	//9998 결과값 없음
	String au011_2 ="/v5/people/info?menu_stb_svc_id=UI50_BSUHD2&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-011";	//9999 파라미터 누락
	String au012_1 ="/v5/inter/cwrelation?menu_stb_svc_id=UI50_BSUHD&menu_id=NM1000018206&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&cw_call_id=TEST.NEW.APIS.PAGE&track_id=368.34727.1522911308391&session_id=b4566b6b-73b9-4f05-ab68-a80ff25a6c95&type=all&epsd_id=CE00123435&epsd_rslu_id=%7B8764D44C-1B04-11E6-B49C-5702654CECC8%7D&IF=IF-NXPG-012";	//9998 결과값 없음
	String au012_2 ="/v5/inter/cwrelation?menu_stb_svc_id=UI50_BSUHD&menu_id=NM1000018206&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&track_id=368.34727.1522911308391&session_id=b4566b6b-73b9-4f05-ab68-a80ff25a6c95&type=all&epsd_id=CE0000005435&epsd_rslu_id=%7B8764D44C-1B04-11E6-B49C-5702654CECC8%7D&IF=IF-NXPG-012";	//9999 파라미터 누락
	String au014_1 ="/v5/contents/gwsynop?menu_stb_svc_id=UI50_BSUHD2&sris_id=CS01152202&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-014";	//9998 결과값 없음
	String au014_2 ="/v5/contents/gwsynop?menu_stb_svc_id=UI50_BSUHD2&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-014";	//9999 파라미터 누락
	String au015_1 ="/v5/contents/commerce?menu_stb_svc_id=UI50_BSUHD2&sris_id=CS10005533&IF=IF-NXPG-015";	//9998 결과값 없음
	String au015_2 ="/v5/contents/commerce?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-015";	//9999 파라미터 누락
	String au016_1 ="/v5/contents/corner?menu_stb_svc_id=UI50_BSUHD2&cnr_grp_id=CN10000157&IF=IF-NXPG-016";	//9998 결과값 없음
	String au016_2 ="/v5/contents/corner?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-016";	//9999 파라미터 누락
	String au401_1 ="/v5/menu/lfthomemapping?menu_stb_svc_id=UI50_BSUHD2&IF=IF-NXPG-401";	//9999 파라미터 누락
	String au403_1 ="/v5/contents/lftsynop?menu_stb_svc_id=UI50_BSUHD2&epsd_id=CE0001268050&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-403";	//9998 결과값 없음
	String au403_2 ="/v5/contents/lftsynop?menu_stb_svc_id=UI50_BSUHD2&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-403";	//9999 파라미터 누락
			
	
	@Test
	public void get003_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au003_1;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get003_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au003_2;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get003_abnormal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au005_1;
		String expected = "9995";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get005_abnormal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au005_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get006_abnormal_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au006_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get006_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au006_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get007_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au007_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get007_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au007_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get008_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au008_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get008_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au008_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get009_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au009_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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
	
	@Test
	public void get009_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au009_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get010_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au010_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get010_abnormal_03_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au010_3;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get010_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au010_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get011_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au011_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get011_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au011_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get012_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au012_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get012_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au012_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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
	


	@Test
	public void get014_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au014_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get014_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au014_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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


	@Test
	public void get015_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au015_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get015_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au015_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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
	

	@Test
	public void get016_abnormal_01_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au016_1;
		String expected = "9998";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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

	@Test
	public void get016_abnormal_02_success() throws JSONException {
		headers.add("Content-Type", "application/json;charset=utf-8");
		headers.add("Accept", "application/json;charset=utf-8");
		String RequestData = au016_2;
		String expected = "9999";
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
				String.class);
		if (null != response) {
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
//	@Test
//	public void get014_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get015_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get016_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get017_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get018_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get101_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get102_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get401_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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
//	@Test
//	public void get403_abnormal_success() throws JSONException {
//		headers.add("Content-Type", "application/json;charset=utf-8");
//		headers.add("Accept", "application/json;charset=utf-8");
//		String RequestData = "";
//		String expected = "9998";
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(RequestData), HttpMethod.GET, entity,
//				String.class);
//		if (null != response) {
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

}
