package com.skb.xpg.appd.svc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Configuration
public class CommonConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(CommonConfiguration.class.getName());
	
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		try {
			StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

			RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			redisTemplate.setKeySerializer(stringRedisSerializer);
			redisTemplate.setHashKeySerializer(stringRedisSerializer);

			return redisTemplate;

		} catch (JedisConnectionException e) {
			if( logger != null){logger.error("Error", e);}
			e.printStackTrace();
			return null;
		}
	}
}
