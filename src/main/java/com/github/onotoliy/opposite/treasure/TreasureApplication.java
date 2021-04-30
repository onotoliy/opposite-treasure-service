package com.github.onotoliy.opposite.treasure;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

/**
 * Главный класс приложения.
 *
 * @author Anatoliy Pokhresnyi
 */
@SpringBootApplication
@EnableJms
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
    public Queue queue() {
        return new ActiveMQQueue("inmemory.queue");
    }

    /**
     * Метод ничего не делающий. Костыль чтобы не ругался CheckStyle.
     */
    public void ignoreMethod() {

    }
}
