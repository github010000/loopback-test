package com.skb.xpg.appd.svc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * Created by Jay Lee on 29/12/2016.
 */
@Configuration
@Profile({"local"})
public class LocalConfiguration {

	@Value("${local.redis.host}")
	private String redisHost;
	
	@Value("${local.redis.port}")
	private int redisPort;
	
	@Value("${local.redis.password}")
	private String password;
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setUsePool(true);
		factory.setTimeout(0);
		factory.setPassword(password);
		return factory;
	}
}

