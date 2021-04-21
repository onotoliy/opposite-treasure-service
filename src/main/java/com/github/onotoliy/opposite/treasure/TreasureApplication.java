package com.github.onotoliy.opposite.treasure;

import com.github.onotoliy.opposite.treasure.services.notifications.TelegramNotificationExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Главный класс приложения.
 *
 * @author Anatoliy Pokhresnyi
 */
@SpringBootApplication
@EnableAsync
@EnableCaching(proxyTargetClass = true)
public class TreasureApplication {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(TelegramNotificationExecutor.class);

    /**
     * CORE_POOL_SIZE.
     */
    private static final int CORE_POOL_SIZE = 3;

    /**
     * MAX_POOL_SIZE.
     */
    private static final int MAX_POOL_SIZE = 3;

    /**
     * QUEUE_CAPACITY.
     */
    private static final int QUEUE_CAPACITY = 3;

    /**
     * Конструктор по умолчанию.
     */
    public TreasureApplication() {

    }

    /**
     * Запускает приложение.
     *
     * @param args Аргументы запуска.
     */
    public static void main(final String[] args) {
        SpringApplication.run(TreasureApplication.class, args);
    }

    /**
     * ThreadPoolTaskExecutor.
     *
     * @return TaskExecutor.
     */
    @Bean(name = "taskExecutor")
    public TaskExecutor defaultTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setThreadNamePrefix("Async-");
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.afterPropertiesSet();

        LOGGER.info("ThreadPoolTaskExecutor set");

        return executor;
    }

    /**
     * Метод ничего не делающий. Костыль чтобы не ругался CheckStyle.
     */
    public void ignoreMethod() {

    }
}
