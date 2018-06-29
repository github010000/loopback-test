package com.skb.xpg.nxpg.svc.config;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
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

}
