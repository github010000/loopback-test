package com.skb.xpg.nxpg.svc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
@RefreshScope
public class RestClient {
	
	@Autowired
	@Qualifier("activeProfile")
	private String activeProfile;

	@Value("${user.cw.socktimeout}")
	private int socktimeout;

	@Value("${user.cw.conntimeout}")
	private int conntimeout;

	@Value("${user.cw.connreqtimeout}")
	private int connreqtimeout;
	
	public String cwUrl = "";
	public boolean sendBff = true;

	public Map<String, Object> apacheGet(String url, Map<String, String> reqparam) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {
				
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet(url);

				RequestConfig requestConfig = RequestConfig.custom()
				          .setSocketTimeout(socktimeout)
				          .setConnectTimeout(conntimeout)
				          .setConnectionRequestTimeout(connreqtimeout)
				          .build();

				request.setConfig(requestConfig);
				
				
				String encoding;
				try {
					encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes("UTF-8"));
					request.addHeader("Authorization", "Basic " + encoding);
				} catch (UnsupportedEncodingException e2) {
					// TODO Auto-generated catch block
					LogUtil.error(reqparam.get("IF"), "", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "NULL", e2.getStackTrace()[0].toString());
				}

				LogUtil.tlog(reqparam.get("IF"), "SEND.REQ", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", reqparam);
				

				HttpResponse response = null;
				BufferedReader rd = null;
				StringBuffer result = new StringBuffer();
				
				try {
					if(client == null) return null;
					response = client.execute(request);
//					System.out.println("Response Code : "  + response.getStatusLine().getStatusCode());
					if (response==null) return null;
					rd = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
					
					String line = "";
					//LogUtil.info(reqparam.get("IF"), "", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", "status : " + response.getStatusLine().getStatusCode() + ", milisecond : " + ((end - before) / 1000000));
					
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
				} catch (ClientProtocolException e1) {
					LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e1.getStackTrace()[0].toString());
				} catch (IOException e1) {
					LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e1.getStackTrace()[0].toString());
				} catch (UnsupportedOperationException e) {
					LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
				} finally {
					try {
						if(rd!=null) rd.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
					}
				}

				String statusCode = "";
				if (response != null) {
					statusCode = response.getStatusLine().getStatusCode() + "";
					resultMap.put("status", statusCode);
				}
				
//				String restregex="\"status\"[\\s]*:[\\s]*(\\{.*?\\})";
//				String codeValue = StrUtil.getRegexString(restregex, result.toString());

				reqparam.put("status", statusCode);
				
				return result.toString();
				
			}
		});

		
		threadPool.execute(task);
		String result = "";
		try {
			try {
				result = task.get(conntimeout, TimeUnit.MILLISECONDS);
				resultMap.put("result", result);
				LogUtil.tlog(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", resultMap);
				
			} catch (TimeoutException e) {
				LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
			}
		} catch (InterruptedException e) {
			LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
		} catch (ExecutionException e) {
			LogUtil.error(reqparam.get("IF"), "RECV.RES", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", e.getStackTrace()[0].toString());
		}
		
		
		return resultMap;
		
	}
	
//	public String getRestUri(String uri) {
//		restTemplate = new RestTemplate();
//		return restTemplate.getForObject(uri, String.class);
//	}
	
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
	public Map<String, Object> getRestUri(String uri, String user, String password, String param, Map<String, String> reqparam) {
		
		
		Map<String, Object> resultMap = null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cwUrl + uri);

		
		if (param != null && builder != null) {
			
			String[] arrKey = param.split(";");
			if (arrKey != null || !param.isEmpty()) {
				for (int y = 0; y < arrKey.length; y++) {
					builder.queryParam(arrKey[y].replaceAll("(.*)\\|(.*)", "$1"), arrKey[y].replaceAll("(.*)\\|(.*)", "$2"));
				}
			}
			
//				System.out.println(builder.build().encode().toUri());
//				if (activeProfile.contains("dev") || activeProfile.contains("stg") || activeProfile.contains("local")) {
//					LogUtil.info(reqparam.get("IF"), "SEND.REQ", reqparam.get("UUID"), reqparam.get("cw_stb_id"), "CW", builder.build().encode().toUri().toString());
//				}

			resultMap = apacheGet(builder.build().encode().toUri().toString(), reqparam);
				

		}
		
		return resultMap;
		
	}
	
	
	
}
