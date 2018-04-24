package com.skb.xpg.nxpg.svc.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.skb.xpg.nxpg.svc.util.LogUtil;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Configuration
@Profile({"local", "dev"})
public class PrimarySingleConfiguration {

	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	@Value("${spring.redis.password}")
	private String password;

    @Primary
	@Bean(name = "primaryJedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setPassword(password);
		factory.setUsePool(true);
		factory.setTimeout(50);
		return factory;
	}

    @Primary
	@Bean(name = "primaryRedisScript")
	public RedisScript<String> testScript() {
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<String>();
		redisScript.setScriptText("local aa = redis.call('HGET', KEYS[1], ARGV[1]) "
								+ "local bb = cjson.decode(aa)['epsd_rslu_id'] "
								+ "return redis.call('HGET', KEYS[2], bb)");
		redisScript.setResultType(String.class);
		return redisScript;
	}

    @Primary
	@Bean(name = "primaryRedisTemplate")
	public RedisTemplate<String, Object> redisTemplate() {
		try {
			StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

			RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory());
			redisTemplate.setKeySerializer(stringRedisSerializer);
			redisTemplate.setValueSerializer(stringRedisSerializer);
			redisTemplate.setHashKeySerializer(stringRedisSerializer);
			redisTemplate.setHashValueSerializer(stringRedisSerializer);

			return redisTemplate;

		} catch (JedisConnectionException e) {
			LogUtil.error("", "", "", "", "", "", "", e.toString());
			return null;
		}
	}
}