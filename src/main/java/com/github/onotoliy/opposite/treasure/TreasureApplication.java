package com.github.onotoliy.opposite.treasure;

import com.github.onotoliy.opposite.treasure.services.notifications.schedule.NotificationObject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Главный класс приложения.
 *
 * @author Anatoliy Pokhresnyi
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
public class TreasureApplication {

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
     * Создание очереди.
     *
     * @return Очередь.
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Deque<NotificationObject> deque() {
        return new ArrayDeque<>();
    }

    /**
     * Метод ничего не делающий. Костыль чтобы не ругался CheckStyle.
     */
    public void ignoreMethod() {

    }
}
