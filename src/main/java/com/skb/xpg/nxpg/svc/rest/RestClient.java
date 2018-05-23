package com.skb.xpg.nxpg.svc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
public class RestClient {

	@Autowired
	private Properties properties;
	
	private RestTemplate restTemplate;
	
	public String cwUrl = "";
	public boolean sendBff = true;

	public String apacheGet(String url) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		RequestConfig requestConfig = RequestConfig.custom()
		          .setSocketTimeout(CastUtil.getObjectToInteger(properties.getCw().get("socktimeout")))
		          .setConnectTimeout(CastUtil.getObjectToInteger(properties.getCw().get("conntimeout")))
		          .setConnectionRequestTimeout(CastUtil.getObjectToInteger(properties.getCw().get("connreqtimeout")))
		          .build();

		request.setConfig(requestConfig);
		
		
		String encoding;
		try {
			encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes("UTF-8"));
			request.addHeader("Authorization", "Basic " + encoding);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			LogUtil.error("", "", "", "", "",e2.toString());
		}
		
		
		HttpResponse response;
		BufferedReader rd = null;
		StringBuffer result = new StringBuffer();
		
		long before = 0;
		long end = 0;
		try {
			if(client == null) return null;
			before = System.nanoTime();
			response = client.execute(request);
			end = System.nanoTime();
//			System.out.println("Response Code : "  + response.getStatusLine().getStatusCode());
			if (response==null) return null;
			rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			LogUtil.info("", "", "", "", "CW", "status : " + response.getStatusLine().getStatusCode() + ", milisecond : " + ((end - before) / 1000000));
			
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (ClientProtocolException e1) {
			LogUtil.error("", "", "", "", "CW",e1.toString());
		} catch (IOException e1) {
			LogUtil.error("", "", "", "", "CW",e1.toString());
		} catch (UnsupportedOperationException e) {
			LogUtil.error("", "", "", "", "CW",e.toString());
		} finally {
			try {
				if(rd!=null) rd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogUtil.error("", "", "", "", "CW",e.toString());
			}
		}
		
		return result.toString();
	}
	
	public String getRestUri(String uri) {
		restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}
	
//	public String getRestUri(String uri, String user, String password, String param, Map<String, String> expand) {
//		restTemplate = new RestTemplate();
//		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(user, password));
//
//		String json = "";
////		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cwUrl + uri);
//
//		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https")
//                .host(cwUrl)
//                .path(uri);
//		
//		if (param != null && builder != null) {
//
//			String[] arrKey = param.split(";");
//			if (arrKey != null || !param.isEmpty()) {
//				for (int y = 0; y < arrKey.length; y++) {
//					builder.queryParam(arrKey[y].replaceAll("(.*)\\|(.*)", "$1"), arrKey[y].replaceAll("(.*)\\|(.*)", "$2"));
//				}
//			}
//
//			try {
//				System.out.println(builder.build().encode().toUri());
//				json = restTemplate.getForObject(builder.build().expand(expand).encode().toUri(), String.class);
//			} catch (RestClientException e) {
//				e.printStackTrace();
//			}
////			if (json != null) {
////				json = json.replaceAll("(^.*item\":)(.*)(\\}\\}$)", "$2");
////			}
//		}
//
//		return json;
//
//	}
	public String getRestUri(String uri, String user, String password, String param) {
		restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(user, password));
		
		String json = "";
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cwUrl + uri);

		
		if (param != null && builder != null) {
			
			String[] arrKey = param.split(";");
			if (arrKey != null || !param.isEmpty()) {
				for (int y = 0; y < arrKey.length; y++) {
					builder.queryParam(arrKey[y].replaceAll("(.*)\\|(.*)", "$1"), arrKey[y].replaceAll("(.*)\\|(.*)", "$2"));
				}
			}
			
			try {
//				System.out.println(builder.build().encode().toUri());
//				LogUtil.info("", "", "", "", "CW", builder.build().encode().toUri().toString());

				json = apacheGet(builder.build().encode().toUri().toString());
			} catch (RestClientException e) {
				LogUtil.error("", "", "", "", "CW",e.toString());
			}
		}
		
		return json;
		
	}
	
	
	
}
