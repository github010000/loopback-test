package com.skb.xpg.nxpg.svc.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
@EnableAsync
@Profile({"stg", "dojstg", "stgtest"})
public class ThreadConfiguration {

	@Value("${task.threadPoolTaskExecutor.corePoolSize}")
	private int qCorePoolSize;

	@Value("${task.threadPoolTaskExecutor.maxPoolSize}")
	private int qMaxPoolSize;

	@Value("${task.threadPoolTaskExecutor.queueCapacity}")
	private int qQueueCapacity;
	
	@Bean(name = "executorService")
	public Executor queueThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(qCorePoolSize);
        executor.setMaxPoolSize(qMaxPoolSize);
        executor.setQueueCapacity(qQueueCapacity);
        executor.initialize();
        return executor;
	}

	@Value("${task.fixedThreadPool.maxCount}")
	private int maxCount;

	@Bean(name = "fixedThreadPool")
	public ExecutorService FixedThreadPool() {
		ExecutorService execService = Executors.newFixedThreadPool(maxCount);
		return execService;
	}
}
