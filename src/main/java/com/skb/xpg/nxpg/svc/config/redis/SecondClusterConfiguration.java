package com.skb.xpg.nxpg.svc.config.redis;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.skb.xpg.nxpg.svc.util.LogUtil;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
@Profile({"stg", "prdsuy", "prdssu"})
public class SecondClusterConfiguration {

    @Value("${second.redis.host}")
    private String redisclusterNodes;
    
    @Value("${second.redis.password}")
    private String redisPassword;

    @Value("${services.redis.connection.timeout}")
    private int clusterConnectionTimeout;

    @Value("${services.redis.redirection.count}")
    private int clusterRedirectionCount;

    @Bean(name="secondRedisCluster")
    public RedisClusterConfiguration getRmsClusterConfiguration() {
        Map<String, Object> source = new HashMap<String, Object>();
        
        source.put("spring.redis.cluster.nodes", redisclusterNodes);
        source.put("spring.redis.cluster.timeout", clusterConnectionTimeout);
        source.put("spring.redis.cluster.max-redirects", clusterRedirectionCount);
        
       return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
    }
    
    @Bean(name="secondJedisConnection")
    public JedisConnectionFactory rmsJedisConnectionFactory() {
    	JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(600);
		poolConfig.setMaxIdle(60);
		poolConfig.setMinIdle(30);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(true);
		
		JedisConnectionFactory factory = new JedisConnectionFactory(getRmsClusterConfiguration(), poolConfig);
		factory.setPassword(redisPassword);
    	return factory;
    }

    @Bean(name="secondRedisTemplate")
	public RedisTemplate<String, Object> rmsRedisTemplate() {
		try {
			StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

			RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
			redisTemplate.setConnectionFactory(rmsJedisConnectionFactory());
			redisTemplate.setKeySerializer(stringRedisSerializer);
			redisTemplate.setHashKeySerializer(stringRedisSerializer);

			return redisTemplate;

		} catch (JedisConnectionException e) {
			LogUtil.error("", "", "", "", "", e.toString());
			return null;
		}
	}

	//
	@Bean(name="second-propertySourcesPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer RmsPropertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
