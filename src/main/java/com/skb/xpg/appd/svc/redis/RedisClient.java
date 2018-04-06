package com.skb.xpg.appd.svc.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.util.StringUtils;


@Service
public class RedisClient {
	private static final Logger logger = LoggerFactory.getLogger(RedisClient.class.getName());
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private Tracer tracer;
	
    public Boolean hexists(String key, String field) {
        return redisTemplate.<String, Object>opsForHash().hasKey(key, field);
    }

    public Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
    
    public String type(String key) {
        return redisTemplate.type(key).toString();
    }
    
    public Long hlen(String key) {
        return redisTemplate.<String, Object>opsForHash().size(key);
    }
    
    public Long strlen(String key) {
        return redisTemplate.opsForValue().size(key);
    }
    
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    public Cursor<Entry<String, Object>>  hscan(String key) {
    	ScanOptions options = ScanOptions.scanOptions().count(1L).build();
        return redisTemplate.<String, Object>opsForHash().scan(key, options);
    }
    
    public Set<String> hkeys(String key) {
        return redisTemplate.<String, Object>opsForHash().keys(key);
    }
    
    public Object hget(String key, String field) {
    	Object obj = null;
    	Span span = null;
    	try {
        	span = this.tracer.createSpan("redis"); 
        	tracer.addTag("redis", key);
        	obj = redisTemplate.<String, Object>opsForHash().get(key, field);
    	} catch (Exception e) {
    		if (logger != null) logger.error(StringUtils.getLogString("hget", "BACH", null, e.toString()));
    	} finally {
    		this.tracer.close(span);
    	}
    	
        return obj;
        
    }

    public Map<String, Object> hgetMap(String key, String field) {
    	Map<String, Object> obj = null;
    	Span span = null;
		try {
			span = this.tracer.createSpan("redis");
			tracer.addTag("redis", key);
			obj = redisTemplate.<String, Map<String, Object>>opsForHash().get(key, field);
		} catch (Exception e) {
			if (logger != null) logger.error(StringUtils.getLogString("hgetMap", "BACH", null, e.toString()));
    	} finally {
    		this.tracer.close(span);
    	}
		return obj;
    }

    public List<Map<String, Object>> hgetList(String key, String field) {
		List<Map<String, Object>> obj = null;
		Span span = null;
		try {
			span = this.tracer.createSpan("redis");
			tracer.addTag("redis", key);
			obj = redisTemplate.<String, List<Map<String, Object>>>opsForHash().get(key, field);
		} catch (Exception e) {
			if (logger != null) logger.error(StringUtils.getLogString("hgetList", "BACH", null, e.toString()));
    	} finally {
    		this.tracer.close(span);
    	}
		return obj;

    }

    public void hdel(String key, String field) {
        redisTemplate.<String, Object>opsForHash().delete(key, field);
    }

    public void hset(String key, String field, Object value) {
        redisTemplate.<String, Object>opsForHash().put(key, field, value);
    }

    /*
    public void quit() {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.close();
                return null;
            }
        });
    }
    */

    public void setbit(final String key, final Long offset, final Boolean value) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setBit(key.getBytes(), offset, value);
                return null;
            }
        });
    }

    public Boolean getbit(final String key, final Long offset) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.getBit(key.getBytes(), offset);
            }
        });
    }
    
    public void sunionstore(String key, Collection<String> keys, String destination) {
        redisTemplate.opsForSet().unionAndStore(key, keys, destination);
    }

    public String echo(final String value) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return new String(connection.echo(value.getBytes()));
            }
        });
    }

    public String ping() {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }
}
