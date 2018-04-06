package com.skb.xpg.appd.svc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class ZWHttpCllient {
	private static final Logger logger = LoggerFactory.getLogger(GridComparator.class.getName());
	
    /*
            client.getParams().setParameter("http.protocol.expect-continue", false);
		client.getParams().setParameter("http.connection.timeout", 500);
		client.getParams().setParameter("http.socket.timeout", 500);
	 */

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        // is this not expensive that every time we are creating this new object?
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(500);
        factory.setConnectTimeout(500);

        return factory;
    }

    public String callURL(String value) throws Exception {
        ClientHttpRequestFactory delegate = getClientHttpRequestFactory();
        URI uri = new URI(value);

        ClientHttpRequest req = delegate.createRequest(uri, HttpMethod.GET);

        ClientHttpResponse resp = req.execute();
        StringBuilder rslt = new StringBuilder("");
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(resp.getBody()));

            while ((line = br.readLine()) != null) {
                rslt.append(line);
            }
        } catch (IOException ie) {
        	logger.error("Error", ie);
		} finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException ioe) {
                	logger.error("Error", ioe);
                }
            }
        }

        return rslt.toString();
    }
}



		/*		String rslt="";
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.expect-continue", false);
		client.getParams().setParameter("http.connection.timeout", 500);
		client.getParams().setParameter("http.socket.timeout", 500);
		HttpGet url = new HttpGet(value);
		HttpResponse resp = client.execute(url);

		HttpEntity entity = resp.getEntity();

		BufferedReader br;
		String line;

		try{
			br = new BufferedReader(new InputStreamReader(entity.getContent()));
			while((line = br.readLine()) != null){
				rslt = rslt+line;
			}
		}finally{
			if(br != null)
				try{
					br.close();
				}catch (Exception e) {
				// TODO: handle exception
			}
		}

		client.getConnectionManager().shutdown();
		return rslt;
	}*/
