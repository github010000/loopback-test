package com.skb.xpg.nxpg.svc.rest;

import java.util.Map;

import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestClient {

	RestTemplate restTemplate;

	public String getRestUri(String uri, Map<String, String> msg, String param) {
		restTemplate = new RestTemplate();
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

	
}
