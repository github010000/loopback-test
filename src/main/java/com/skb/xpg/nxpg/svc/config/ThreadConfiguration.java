package com.skb.xpg.nxpg.svc.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
public class ThreadConfiguration {

	@Bean(name = "executorService")
	public ExecutorService FixedThreadPool() {
		ExecutorService execService = Executors.newFixedThreadPool(100);
		return execService;
	}

}
