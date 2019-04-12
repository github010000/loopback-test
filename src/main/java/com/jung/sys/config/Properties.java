package com.jung.sys.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by dmshin on 06/02/2017.
 */
@ConfigurationProperties(value = "user")
public class Properties {
	
    private Map<String, Object> results = new HashMap<>();

    public Map<String, Object> getResults() {
        return results;
    }

    private Map<String, List<String>> scripts = new HashMap<>();

    public Map<String, List<String>> getScripts() {
        return scripts;
    }

    private Map<String, Object> api = new HashMap<>();

    public Map<String, Object> getApi() {
        return api;
    }
    
    private Map<String, String> defaults = new HashMap<>();

    public Map<String, String> getDefaults() {
        return defaults;
    }

    private Map<String, Object> cw = new HashMap<>();
    
    public Map<String, Object> getCw() {
    	return cw;
    }
    private Map<String, String> checkdate = new HashMap<>();
    
    public Map<String, String> getCheckdate() {
    	return checkdate;
    }
}
