package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import com.github.onotoliy.opposite.treasure.data.page.Meta;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.data.page.Paging;
import com.github.onotoliy.opposite.treasure.repositories.core.ReaderRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.Record;

/**
 * Базовый сервис чтения объектов.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @param <R> Запись из БД
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractReaderService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends ReaderRepository<P>>
implements ReaderService<E, P> {

    /**
     * Репозиторий.
     */
    protected final R repository;

    /**
     * Констрктор.
     *
     * @param repository Репозиторий.
     */
    public AbstractReaderService(final R repository) {
        this.repository = repository;
    }

    /**
     * Преобразование записи из БД {@link Record} в объект {@link E}.
     *
     * @param record Запись.
     * @return Объект.
     */
    protected abstract E toDTO(Record record);

    @Override
    public E get(final UUID uuid) {
        return toDTO(repository.get(uuid));
    }

    @Override
    public Page<E> getAll(final P parameter) {
        final List<E> list = repository
            .getAll(parameter)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        final int count = repository.count(parameter);

        return new Page<>(
            new Meta(count, new Paging(parameter.offset(), parameter.numberOfRows())),
            list
        );
    }

}
