package com.skb.xpg.nxpg.svc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.skb.xpg.nxpg.svc.util.LogUtil;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Configuration
@Profile({"local", "dev", "jenkins"})
public class DevConfiguration {

	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	@Value("${spring.redis.password}")
	private String password;
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
    
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

	@Bean
	public RedisScript<String> testScript() {
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<String>();
		redisScript.setScriptText("local aa = redis.call('HGET', KEYS[1], ARGV[1]) "
								+ "local bb = cjson.decode(aa)['epsd_rslu_id'] "
								+ "return redis.call('HGET', KEYS[2], bb)");
		redisScript.setResultType(String.class);
		return redisScript;
	}

	@Bean
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
//			LogUtil.error(e.getStackTrace(), "NULL");
			LogUtil.error(e.getStackTrace(), "", "", "", "", "", "NULL");
			return null;
		}
	}
}