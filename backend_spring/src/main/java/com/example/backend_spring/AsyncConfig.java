package com.example.backend_spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class AsyncConfig {

    @Bean(name = "customThreadPoolTaskScheduler")
    public ThreadPoolTaskScheduler customThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 최대 스레드 수 설정
        scheduler.setThreadNamePrefix("MyScheduler-");
        scheduler.setRemoveOnCancelPolicy(true); // 작업 취소 시 큐에서 제거
        scheduler.setAwaitTerminationSeconds(60); // 종료 대기 시간
        return scheduler;
    }

    @Bean(name = "customThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor customThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);        // 최소 스레드 개수
        executor.setMaxPoolSize(10);       // 최대 스레드 개수
        executor.setQueueCapacity(Integer.MAX_VALUE);    // 큐 크기
        executor.setThreadNamePrefix("Pool-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }

}
