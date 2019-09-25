package com.github.onotoliy.opposite.treasure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения.
 *
 * @author Anatoliy Pokhresnyi
 */
@SpringBootApplication
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
     * Метод ничего не делающий. Костыль чтобы не ругался CheckStyle.
     */
    public void ignoreMethod() {

    }
}
