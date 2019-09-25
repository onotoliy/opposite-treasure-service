package com.github.onotoliy.opposite.treasure.dto;

import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import java.util.UUID;

/**
 * Поисковые параметры для транзакции.
 *
 * @author Anatoliy Pokhresnyi
 */
public class TransactionSearchParameter extends SearchParameter {

    /**
     * Название.
     */
    private final String name;

    /**
     * Пользователь.
     */
    private final UUID user;

    /**
     * Событие.
     */
    private final UUID event;

    /**
     * Тип транзакции.
     */
    private final TransactionType type;

    /**
     * Контструктор.
     *
     * @param name Название.
     * @param user Пользователь
     * @param event Событие.
     * @param type Тип транзакции.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     */
    public TransactionSearchParameter(final String name,
                                      final UUID user,
                                      final UUID event,
                                      final TransactionType type,
                                      final int offset,
                                      final int numberOfRows) {
        super(offset, numberOfRows);
        this.name = name;
        this.user = user;
        this.event = event;
        this.type = type;
    }

    /**
     * Проверяет необходимо ли производить поиск по названию.

     * @return Результат проверки.
     */
    public boolean hasName() {
        return Strings.nonEmpty(name);
    }

    /**
     * Получает название.
     *
     * @return Название.
     */
    public String getName() {
        return name;
    }

    /**
     * Проверяет необходимо ли производить поиск по пользователю.

     * @return Результат проверки.
     */
    public boolean hasUser() {
        return GUIDs.nonEmpty(user);
    }

    /**
     * Получает пользователя.
     *
     * @return Пользователь.
     */
    public UUID getUser() {
        return user;
    }

    /**
     * Проверяет необходимо ли производить поиск по событию.

     * @return Результат проверки.
     */
    public boolean hasEvent() {
        return GUIDs.nonEmpty(event);
    }

    /**
     * Получает событие.
     *
     * @return Событие.
     */
    public UUID getEvent() {
        return event;
    }

    /**
     * Проверяет необходимо ли производить поиск по типу транзакции.

     * @return Результат проверки.
     */
    public boolean hasType() {
        return Objects.nonEmpty(type)
            && Objects.nonEqually(type, TransactionType.NONE);
    }

    /**
     * Получает тип транзакции.
     *
     * @return Тип транзакции.
     */
    public TransactionType getType() {
        return type;
    }
}
