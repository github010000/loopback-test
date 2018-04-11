package com.skb.xpg.nxpg.svc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
@ComponentScan(basePackages={"com.skb.xpg.nxpg.svc.config"})
public class CommonConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Value("${user.cw.baseurl}")
	private String cwBaseUrl;
	
	@Value("${user.cw.user}")
	private String cwUser;
	
	@Value("${user.cw.password}")
	private String cwPassword;
	
	@Bean(name = "cwBaseUrl")
	public String getCwBaseUrl() {
		return cwBaseUrl;
	}
	
	@Bean(name = "cwUser")
	public String getCwUser() {
		return cwUser;
	}

	@Bean(name = "cwPassword")
	public String getCwPassword() {
		return cwPassword;
	}
	
	@Bean()
    @RefreshScope
    public Properties properties() {
        return new Properties();
    }
	
	
	@Bean(name = "activeProfile")
	public String getActiveProfile() {
		String[] profiles = environment.getActiveProfiles();
		if (profiles != null) {
			if (profiles.length > 0) {
				return profiles[0];
			}
		}
		return "";
	}


	
	/*
	@Bean(name = "cmsRestTemplate")
	public RestTemplate cmsRestTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(10 * 1000);
		factory.setReadTimeout(10 * 1000);
		RestTemplate restTemplate = new RestTemplate(factory);
		return restTemplate;
	}

	@Bean(name = "cwRestTemplate")
	public RestTemplate cwRestTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(500);
		factory.setReadTimeout(500);
		RestTemplate restTemplate = new RestTemplate(factory);
		return restTemplate;
	}
	*/
}
