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
import org.jooq.impl.DSL;

import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

public abstract class AbstractReaderRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends Record,
    T extends Table<R>>
implements ReaderRepository<E, P> {

    protected final T TABLE;

    protected final TableField<R, UUID> UUID;

    protected final TableField<R, String> NAME;

    protected final TableField<R, UUID> AUTHOR;

    protected final TableField<R, Timestamp> CREATION_DATE;

    protected final TableField<R, Timestamp> DELETION_DATE;

    protected final DSLContext dsl;

    protected final UserRPC user;

    protected AbstractReaderRepository(T table,
                                       TableField<R, UUID> uuid,
                                       TableField<R, String> name,
                                       TableField<R, UUID> author,
                                       TableField<R, Timestamp> creationDate,
                                       TableField<R, Timestamp> deletionDate,
                                       DSLContext dsl,
                                       UserRPC user) {
        this.TABLE = table;
        this.UUID = uuid;
        this.NAME = name;
        this.AUTHOR = author;
        this.CREATION_DATE = creationDate;
        this.DELETION_DATE = deletionDate;
        this.dsl = dsl;
        this.user = user;
    }

    protected abstract E toDTO(Record record);

    protected SelectJoinStep<Record> findQuery() {
        return dsl.select().from(TABLE);
    }

    @Override
    public Optional<E> getOptional(UUID uuid) {
        return findQuery().where(UUID.eq(uuid)).fetchOptional(this::toDTO);
    }

    @Override
    public E get(UUID uuid) {
        return getOptional(uuid).orElseThrow(
            () -> new NotFoundException(TABLE, uuid));
    }

    @Override
    public List<Option> getAll() {
        return findQuery().fetch(record -> new Option(GUID.format(record, UUID), STRING.format(record, NAME)));
    }

    @Override
    public Page<E> getAll(P parameter) {
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

    protected List<? extends OrderField<?>> orderBy() {
        return new LinkedList<>(Collections.singleton(CREATION_DATE.desc()));
    }

    protected List<Condition> where(P parameter) {
        return new LinkedList<>(Collections.singleton(DSL.trueCondition()));
    }

    protected Option formatUser(Record record, Field<UUID> field) {
        return Optional.ofNullable(record.getValue(field, java.util.UUID.class))
                       .map(user::findOption)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .orElse(null);
    }
}
