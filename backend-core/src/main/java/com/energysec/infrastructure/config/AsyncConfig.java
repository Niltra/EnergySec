package com.energysec.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {

    @PostConstruct
    public void enableInheritableSecurityContext() {
        // Ensures that spawned threads (e.g., Virtual Threads) inherit the SecurityContext
        // from the parent thread, crucial for @Async methods that rely on our DataMaskingAspect or Roles.
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        // Configure Spring's standard async executor to use Java 21 Virtual Threads
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        // Configure Tomcat to handle incoming HTTP requests using Virtual Threads
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
