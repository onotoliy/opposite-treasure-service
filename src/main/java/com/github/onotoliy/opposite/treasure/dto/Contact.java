package com.github.onotoliy.opposite.treasure.dto;

/**
 * Контактная информация пользователя.
 *
 * @author Anatoliy Pokhresnyi
 */
public class Contact {
    /**
     * Уникальный идентификатор пользователя.
     */
    private final String uuid;

    /**
     * Электронный адрес.
     */
    private final String email;

    /**
     * Отправлять уведомления по электронной почты.
     */
    private final boolean notifyByEmail;

    /**
     * Номер телефона.
     */
    private final String phone;

    /**
     * Отправлять уведомления по телефону.
     */
    private final boolean notifyByPhone;

    /**
     * Чат в Telegram.
     */
    private final String telegram;

    /**
     * Отправлять уведомления по Telegram.
     */
    private final boolean notifyByTelegram;

    /**
     * Токен в Firebase.
     */
    private final String firebase;

    /**
     * Отправлять уведомления в Firebase.
     */
    private final boolean notifyByFirebase;

    /**
     * Конструктор.
     *
     * @param uuid Уникальный идентификатор пользователя.
     * @param email Электронный адрес.
     * @param notifyByEmail Отправлять уведомления по электронной почты.
     * @param phone Номер телефона.
     * @param notifyByPhone Отправлять уведомления по телефону.
     * @param telegram Чат в Telegram.
     * @param notifyByTelegram Отправлять уведомления по Telegram.
     * @param firebase Токен в Firebase.
     * @param notifyByFirebase Отправлять уведомления в Firebase.
     */
    public Contact(final String uuid,
                   final String email,
                   final boolean notifyByEmail,
                   final String phone,
                   final boolean notifyByPhone,
                   final String telegram,
                   final boolean notifyByTelegram,
                   final String firebase,
                   final boolean notifyByFirebase
    ) {
        this.uuid = uuid;
        this.email = email;
        this.notifyByEmail = notifyByEmail;
        this.phone = phone;
        this.notifyByPhone = notifyByPhone;
        this.telegram = telegram;
        this.notifyByTelegram = notifyByTelegram;
        this.firebase = firebase;
        this.notifyByFirebase = notifyByFirebase;
    }

    /**
     * Получает уникальный идентификатор пользователя.
     *
     * @return Уникальный идентификатор пользователя.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Получает электронный адрес.
     *
     * @return Электронный адрес.
     */
    public String getEmail() {
        return email;
    }

    /**
     *  Отправлять уведомления по электронной почты.
     *
     * @return Отправлять уведомления по электронной почты.
     */
    public boolean isNotifyByEmail() {
        return notifyByEmail;
    }

    /**
     * Получает номер телефона.
     *
     * @return Номер телефона.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Отправлять уведомления по телефону.
     *
     * @return Отправлять уведомления по телефону.
     */
    public boolean isNotifyByPhone() {
        return notifyByPhone;
    }

    /**
     * Получает чат в Telegram.
     *
     * @return Чат в Telegram.
     */
    public String getTelegram() {
        return telegram;
    }

    /**
     * Отправлять уведомления по Telegram.
     *
     * @return Отправлять уведомления по Telegram.
     */
    public boolean isNotifyByTelegram() {
        return notifyByTelegram;
    }

    /**
     * Получает токен в Firebase.
     *
     * @return Токен в Firebase.
     */
    public String getFirebase() {
        return firebase;
    }

    /**
     * Отправлять уведомления в Firebase.
     *
     * @return Отправлять уведомления в Firebase.
     */
    public boolean isNotifyByFirebase() {
        return notifyByFirebase;
    }
}
