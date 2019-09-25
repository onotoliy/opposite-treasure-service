package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Meta;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.data.page.Paging;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.TableField;

import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

/**
 * Базовый репозиторий чтения записей из БД.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @param <R> Запись из БД
 * @param <T> Таблица в БД
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractReaderRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends Record,
    T extends Table<R>>
implements ReaderRepository<E, P> {

    /**
     * Таблица.
     */
    protected final T TABLE;

    /**
     * Уникальный идентификатор.
     */
    protected final TableField<R, UUID> UUID;

    /**
     * Название.
     */
    protected final TableField<R, String> NAME;

    /**
     * Автор.
     */
    protected final TableField<R, UUID> AUTHOR;

    /**
     * Дата создания.
     */
    protected final TableField<R, Timestamp> CREATION_DATE;

    /**
     * Дата удаления.
     */
    protected final TableField<R, Timestamp> DELETION_DATE;

    /**
     * Контекст подключения к БД.
     */
    protected final DSLContext dsl;

    /**
     * Сервис чтения пользователей.
     */
    protected final UserRPC user;

    /**
     * Конструктор.
     *
     * @param table Таблица.
     * @param uuid Уникальный идентификатор.
     * @param name Название.
     * @param author Автор.
     * @param creationDate Дата создания.
     * @param deletionDate Дата удаления.
     * @param dsl Контекст подключения к БД.
     * @param user Сервис чтения пользователей.
     */
    protected AbstractReaderRepository(final T table,
                                       final TableField<R, UUID> uuid,
                                       final TableField<R, String> name,
                                       final TableField<R, UUID> author,
                                       final TableField<R, Timestamp> creationDate,
                                       final TableField<R, Timestamp> deletionDate,
                                       final DSLContext dsl,
                                       final UserRPC user) {
        this.TABLE = table;
        this.UUID = uuid;
        this.NAME = name;
        this.AUTHOR = author;
        this.CREATION_DATE = creationDate;
        this.DELETION_DATE = deletionDate;
        this.dsl = dsl;
        this.user = user;
    }

    /**
     * Преобзазование записи из БД в объект.
     *
     * @param record Запись из БД.
     * @return Объект.
     */
    protected abstract E toDTO(final Record record);

    /**
     * Получение select запроса из таблицы.
     *
     * @return Запрос.
     */
    protected SelectJoinStep<Record> findQuery() {
        return dsl.select().from(TABLE);
    }

    @Override
    public Optional<E> getOptional(final UUID uuid) {
        return findQuery().where(UUID.eq(uuid)).fetchOptional(this::toDTO);
    }

    @Override
    public E get(final UUID uuid) {
        return getOptional(uuid).orElseThrow(
            () -> new NotFoundException(TABLE, uuid));
    }

    @Override
    public List<Option> getAll() {
        return findQuery().fetch(record -> new Option(GUID.format(record, UUID), STRING.format(record, NAME)));
    }

    @Override
    public Page<E> getAll(final P parameter) {
        return new Page<>(
            new Meta(
                dsl.selectCount()
                   .from(TABLE)
                   .where(where(parameter))
                   .fetchOptional(0, int.class)
                   .orElse(0),
                new Paging(parameter.offset(), parameter.numberOfRows())),
            findQuery().where(where(parameter))
                       .orderBy(orderBy())
                       .offset(parameter.offset())
                       .limit(parameter.numberOfRows())
                       .fetch(this::toDTO));
    }

    /**
     * Получение колонок по которым будет производиться сортировка.
     *
     * @return Колонки по которым будет производиться сортировка.
     */
    protected List<? extends OrderField<?>> orderBy() {
        return new LinkedList<>(Collections.singleton(CREATION_DATE.desc()));
    }

    /**
     * Получение условий выборки данных из БД.
     *
     * @param parameter Поисковы параметры.
     * @return Условия выборки данных из БД.
     */
    protected List<Condition> where(final P parameter) {
        return new LinkedList<>(Collections.singleton(DELETION_DATE.isNull()));
    }

    /**
     * Преобразование пользователя из уникального идентификатора в объект.
     *
     * @param record Запись из БД.
     * @param field Колонка в которой содержиться уникальный идентификатор пользователя.
     * @return Пользователь.
     */
    protected Option formatUser(final Record record, final Field<UUID> field) {
        return Optional.ofNullable(record.getValue(field, java.util.UUID.class))
                       .map(user::findOption)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .orElse(null);
    }
}
