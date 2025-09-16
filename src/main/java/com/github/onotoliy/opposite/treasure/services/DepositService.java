package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.Position;
import com.github.onotoliy.opposite.treasure.data.page.Meta;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.data.page.Paging;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.repositories.DepositRepository;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис чтения депозитов.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class DepositService {

    /**
     * Репозиторий.
     */
    private final DepositRepository repository;

    /**
     * Репозиторий долгов.
     */
    private final DebtRepository debt;

    /**
     * Сервис управдения пользвателями из Keycloak.
     */
    private final KeycloakService keycloak;

    /**
     * Конструктор.
     *
     * @param keycloak Сервис управдения пользвателями из Keycloak.
     * @param debt Репозиторий управления долгами пользователя.
     * @param repository Репозиторий.
     */
    @Autowired
    public DepositService(
        final KeycloakService keycloak,
        final DebtRepository debt,
        final DepositRepository repository
    ) {
        this.repository = repository;
        this.keycloak = keycloak;
        this.debt = debt;
    }

    /**
     * Получение списка долгов пользователя.
     *
     * @param deposit Уникальный идентификатор депозита.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Список долгов пользователя.
     */
    public Page<Event> getDebts(
        final UUID deposit,
        final int offset,
        final int numberOfRows
    ) {
        int count = debt.countDebts(deposit);
        List<Event> page = debt
            .getDebts(deposit, offset, numberOfRows)
            .stream()
            .map(it ->
                     EventService.toDTO(
                         it,
                         author -> Optional
                             .ofNullable(author)
                             .map(this::get)
                             .map(athr -> new Option(athr.uuid(), athr.name()))
                             .orElse(null)
                     )
            )
            .collect(Collectors.toUnmodifiableList());

        return new Page<>(
            new Meta(count, new Paging(offset, numberOfRows)),
            page
        );
    }

    /**
     * Получение депозита текущего пользователя.
     *
     * @return Депозита текущего пользователя
     */
    public Deposit me() {
        return get(GUIDs.parse("b00c4f68-ed47-45d2-b96f-3d8cf768ea66"));
    }

    /**
     * Получениие депозита.
     *
     * @param uuid Уникальный идентификатор.
     * @return Депозит.
     */
    public Deposit get(final UUID uuid) {
        final UserRepresentation representation = keycloak.get(uuid);

        return toDTO(representation);
    }

    /**
     * Поиск депозитов.
     *
     * @param parameter Поисковые параметры.
     * @return Депозиты.
     */
    public Page<Deposit> getAll(final DepositSearchParameter parameter) {
        final Integer count =  keycloak.count(parameter);
        final List<Deposit> list =  keycloak
                .getAll(parameter)
                .stream()
                .map(representation -> toDTO(representation))
                .collect(Collectors.toUnmodifiableList());

        return new Page<Deposit>(
                new  Meta(
                    count,
                    new Paging(parameter.offset(), parameter.numberOfRows())
                ),
                list
        );
    }

    /**
     * Создание депозита.
     *
     * @param dto Депозит.
     * @return Депозит.
     */
    public Deposit create(final Deposit dto) {
        final UUID uuid = keycloak.create(dto);

        repository.newDeposit(uuid);

        return get(uuid);
    }

    /**
     * Изменение депозита.
     *
     * @param dto Депозит.
     * @return Депозит.
     */
    public Deposit update(final Deposit dto) {
        return get(keycloak.update(dto));
    }

    /**
     * Удаление депозита.
     *
     * @param uuid никальный идентификатор депозита.
     */
    public void delete(final UUID uuid) {
        keycloak.delete(uuid);
    }

    /**
     * Преобразование UserRepresentation в депозит.
     *
     * @param representation UserRepresentation.
     * @return Депозит.
     */
    private Deposit toDTO(final UserRepresentation representation) {
        final UUID uuid = GUIDs.parse(representation.getId());

        return new Deposit(
                uuid,
                representation.getUsername(),
                representation.getFirstName(),
                representation.getLastName(),
                representation.firstAttribute("patronymic"),
                repository.money(uuid),
                representation.firstAttribute("logo"),
                representation.getEmail(),
                Dates.toInstant(representation.firstAttribute("birthday")),
                Dates.toInstant(representation.firstAttribute("joiningDate")),
                toPosition(representation.getRealmRoles())
        );
    }

    /**
     * Преобразование роли в должность.
     *
     * @param roles Список ролей.
     * @return Должность.
     */
    private Position toPosition(final List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return Position.NONE;
        }

        Predicate<Position> predicate = position -> roles
            .stream().anyMatch(e -> Strings.equals(e, position.name(), true));

        if (predicate.test(Position.PRESIDENT)) {
            return Position.PRESIDENT;
        }

        if (predicate.test(Position.VICE_PRESIDENT)) {
            return Position.VICE_PRESIDENT;
        }

        if (predicate.test(Position.TREASURER)) {
            return Position.TREASURER;
        }

        if (predicate.test(Position.SECRETARY)) {
            return Position.SECRETARY;
        }

        if (predicate.test(Position.MEMBER)) {
            return Position.MEMBER;
        }

        if (predicate.test(Position.FRIEND)) {
            return Position.FRIEND;
        }

        return Position.NONE;
    }
}
