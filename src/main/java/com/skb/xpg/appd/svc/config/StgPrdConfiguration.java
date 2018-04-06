package com.skb.xpg.appd.svc.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Profile({"stg", "prd"})
public class StgPrdConfiguration {

    @Value("${services.redis.host}")
    private String redisclusterNodes;
    
    @Value("${services.redis.password}")
    private String redisPassword;

    @Value("${services.redis.connection.timeout}")
    private int clusterConnectionTimeout;

    @Value("${services.redis.redirection.count}")
    private int clusterRedirectionCount;
    
    @Bean
    public RedisClusterConfiguration getClusterConfiguration() {
        Map<String, Object> source = new HashMap<String, Object>();
        
        source.put("spring.redis.cluster.nodes", redisclusterNodes);
        source.put("spring.redis.cluster.timeout", clusterConnectionTimeout);
        source.put("spring.redis.cluster.max-redirects", clusterRedirectionCount);
        source.put("spring.redis.cluster.password", redisPassword);
        
       return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
    }
    
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
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
		
		JedisConnectionFactory factory = new JedisConnectionFactory(getClusterConfiguration(), poolConfig);
		factory.setPassword(redisPassword);
		
		return factory;
    }
}
