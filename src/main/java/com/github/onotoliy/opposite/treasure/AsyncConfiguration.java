package com.github.onotoliy.opposite.treasure;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Конфигурация асинхронности для спринга.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(AsyncConfiguration.class);

    /**
     * CORE_POOL_SIZE.
     */
    private static final int CORE_POOL_SIZE = 5;

    /**
     * MAX_POOL_SIZE.
     */
    private static final int MAX_POOL_SIZE = 10;

    /**
     * Executor, который будет выполнять @Async методы.
     *
     * @return нужный executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();

        LOGGER.info("get AsyncExecutor");

        return executor;
    }

    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        LOGGER.info("get AsyncUncaughtExceptionHandler");

        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
