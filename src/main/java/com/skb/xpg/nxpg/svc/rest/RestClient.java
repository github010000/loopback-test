package com.skb.xpg.nxpg.svc.rest;

import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestClient {

	private RestTemplate restTemplate;
	/*
	public String getRestUri(String uri, Map<String, String> msg, String param) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(300);
		factory.setReadTimeout(300);
		restTemplate = new RestTemplate(factory);
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("user", "user123"));

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);

		String[] arrKey = param.split(",");
		String key = "";
		if (arrKey != null && builder != null) {
			for (int y = 0; y < arrKey.length; y++) {
				key = arrKey[y].toUpperCase();
				if (msg.containsKey(key)) {
					builder.queryParam(key, msg.get(key));
				}
			}
		}

		return restTemplate.getForObject(builder.build().encode().toUri(), String.class);
	}

	public String getRestUriSimple(String uri) {
		restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("user", "user123"));

		return restTemplate.getForObject(uri, String.class);
	}
	
	*/
	public String cwUrl = "";
	public boolean sendBff = true;

	public String getRestUri(String uri) {
		restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}
	
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
				System.out.println(builder.build().encode().toUri());
				json = restTemplate.getForObject(builder.build().encode().toUri(), String.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
//			if (json != null) {
//				json = json.replaceAll("(^.*item\":)(.*)(\\}\\}$)", "$2");
//			}
		}

		return json;

	}
	
	
	
}
