package com.github.onotoliy.opposite.treasure;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
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
     * QueueCapacity.
     */
    private static final int QUEUE_CAPACITY = 500;

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
     * Создание TaskExecutor.
     *
     * @return TaskExecutor.
     */
    @Bean(name = "threadPoolTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("TreasureLookup-");
        executor.initialize();

        return executor;
    }

    /**
     * Метод ничего не делающий. Костыль чтобы не ругался CheckStyle.
     */
    public void ignoreMethod() {

    }
}
