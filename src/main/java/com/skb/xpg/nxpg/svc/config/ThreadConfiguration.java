package com.skb.xpg.nxpg.svc.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
public class ThreadConfiguration {

	@Bean(name = "executorService")
	public Executor queueThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
	}
//	public ExecutorService FixedThreadPool() {
//		ExecutorService execService = Executors.newFixedThreadPool(100);
//		return execService;
//	}

}
