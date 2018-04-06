package com.skb.xpg.appd.svc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@Profile({"dev", "jenkins"})
public class DevConfiguration {

	@Value("${services.redis.host}")
	private String redisHost;
	
	@Value("${services.redis.port}")
	private int redisPort;
	
	@Value("${services.redis.password}")
	private String password;
	
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setPassword(password);
		factory.setUsePool(true);
		factory.setTimeout(0);
		return factory;
	}
	
}
