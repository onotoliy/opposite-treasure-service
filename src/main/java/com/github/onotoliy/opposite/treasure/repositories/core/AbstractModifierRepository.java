package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import com.github.onotoliy.opposite.treasure.exceptions.NotUniqueException;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.function.Consumer;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;
import static com.github.onotoliy.opposite.treasure.utils.TimestampUtil.TIMESTAMP;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

public abstract class AbstractModifierRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends Record,
    T extends Table<R>>
extends AbstractReaderRepository<E, P, R, T>
implements ModifierRepository<E, P> {

    protected AbstractModifierRepository(T table,
                                         TableField<R, UUID> uuid,
                                         TableField<R, String> name,
                                         TableField<R, UUID> author,
                                         TableField<R, Timestamp> creationDate,
                                         TableField<R, Timestamp> deletionDate,
                                         DSLContext dsl,
                                         UserRPC user) {
        super(table, uuid, name, author, creationDate, deletionDate, dsl, user);
    }

    @Override
    public void transaction(Consumer<Configuration> consumer) {
        dsl.transaction(consumer::accept);
    }

    @Override
    public E create(E dto) {
        return execute(dto, insertQuery(dto));
    }

    @Override
    public E create(Configuration configuration, E dto) {
        return execute(dto, insertQuery(configuration, dto));
    }

    @Override
    public E update(E dto) {
        return execute(dto, updateQuery(dto).where(UUID.eq(GUID.parse(dto))));
    }

    @Override
    public E update(Configuration configuration, E dto) {
        return execute(dto, updateQuery(configuration, dto).where(UUID.eq(GUID.parse(dto))));
    }

    @Override
    public void delete(UUID uuid) {
        execute(uuid, deleteQuery(uuid));
    }

    @Override
    public void delete(Configuration configuration, UUID uuid) {
        execute(uuid, deleteQuery(configuration, uuid));
    }

    protected UpdateConditionStep<R> deleteQuery(Configuration configuration, UUID uuid) {
        return DSL.using(configuration)
                  .update(TABLE)
                  .set(DELETION_DATE, TIMESTAMP.now())
                  .where(UUID.eq(uuid));
    }

    protected InsertSetMoreStep<R> insertQuery(Configuration configuration, E dto) {
        return DSL.using(configuration)
                  .insertInto(TABLE)
                  .set(UUID, GUID.parse(dto))
                  .set(NAME, STRING.parse(dto.getName()))
                  .set(CREATION_DATE, TIMESTAMP.now())
                  .set(AUTHOR, GUID.parse(dto.getAuthor()));
    }

    protected UpdateSetMoreStep<R> updateQuery(Configuration configuration, E dto) {
        return DSL.using(configuration)
                  .update(TABLE)
                  .set(NAME, STRING.parse(dto.getName()))
                  .set(CREATION_DATE, TIMESTAMP.now())
                  .set(AUTHOR, GUID.parse(dto.getAuthor()));
    }

    protected UpdateConditionStep<R> deleteQuery(UUID uuid) {
        return dsl.transactionResult(
            configuration -> deleteQuery(configuration, uuid));
    }

    protected InsertSetMoreStep<R> insertQuery(E dto) {
        return dsl.transactionResult(
            configuration -> insertQuery(configuration, dto));
    }

    protected UpdateSetMoreStep<R> updateQuery(E dto) {
        return dsl.transactionResult(
            configuration -> updateQuery(configuration, dto));
    }

    private E execute(E dto, Query query) {
        execute(GUID.parse(dto), query);

        return dto;
    }

    private void execute(UUID uuid, Query step) {
        int count = step.execute();

        if (count == 0) {
            throw new NotFoundException(TABLE, uuid);
        }

        if (count > 1) {
            throw new NotUniqueException(TABLE, uuid);
        }
    }
}
