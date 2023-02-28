package com.sally.sns.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer {
	public static final String SCHEDULER_THREAD = "SCHEDULER-THREAD";
	public static final int TASK_SCHEDULER_POOL_SIZE = 10;

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(TASK_SCHEDULER_POOL_SIZE);
		taskScheduler.setThreadNamePrefix(SCHEDULER_THREAD);
		taskScheduler.initialize();
		return taskScheduler;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(taskScheduler());
	}
}
