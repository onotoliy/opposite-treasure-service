package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.repositories.CashboxRepository;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.repositories.DepositRepository;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.repositories.TransactionRepository;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Numbers;
import com.github.onotoliy.opposite.treasure.utils.Objects;

import java.math.BigDecimal;
import java.util.UUID;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис управления транзакциями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class TransactionService
extends AbstractModifierService<
    Transaction,
    TransactionSearchParameter,
    TransactionRepository> {

    /**
     * Репозиторий данных о кассе.
     */
    private final CashboxRepository cashbox;

    /**
     * Репозиторий депозитов.
     */
    private final DepositRepository deposit;

    /**
     * Репозиторий событий.
     */
    private final EventRepository event;

    /**
     * Репозиторий долгов.
     */
    private final DebtRepository debt;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий транзакций.
     * @param cashbox Репозиторий данных о кассе.
     * @param deposit Репозиторий депозитов.
     * @param event Репозиторий событий.
     * @param debt Репозиторий долгов.
     */
    @Autowired
    public TransactionService(final TransactionRepository repository,
                              final CashboxRepository cashbox,
                              final DepositRepository deposit,
                              final EventRepository event,
                              final DebtRepository debt) {
        super(repository);
        this.cashbox = cashbox;
        this.deposit = deposit;
        this.event = event;
        this.debt = debt;
    }

    @Override
    protected void create(final Configuration configuration,
                          final Transaction dto) {
        BigDecimal money = Numbers.parse(dto.getCash());

        if (money == null) {
            throw new ModificationException("Денежные средсва не заполнены");
        }

        validation(dto);

        if (dto.getType() == TransactionType.COST) {
            BigDecimal cashbox = this.cashbox.money();

            if (cashbox == null || cashbox.compareTo(money) < 0) {
                throw new ModificationException(
                    "В кассе не может быть отрицательная сумма");
            }

            this.cashbox.cost(configuration, money);
        }

        if (dto.getType() == TransactionType.CONTRIBUTION) {
            cashbox.contribution(configuration, money);
            debt.contribution(configuration,
                              GUIDs.parse(dto.getPerson()),
                              GUIDs.parse(dto.getEvent()));
        }

        if (dto.getType() == TransactionType.WRITE_OFF) {
            UUID person = GUIDs.parse(dto.getPerson());
            BigDecimal deposit = this.deposit.money(person);

            if (deposit == null || deposit.compareTo(money) < 0) {
                throw new ModificationException(
                    "Депозит не может быть отрицательным");
            }

            this.deposit.cost(configuration, person, money);
            this.debt.contribution(configuration,
                                   person,
                                   GUIDs.parse(dto.getEvent()));
        }

        if (dto.getType() == TransactionType.PAID) {
            deposit.contribution(configuration,
                                 GUIDs.parse(dto.getPerson()),
                                 money);
        }

        repository.create(configuration, dto);
    }

    @Override
    protected void update(final Configuration configuration,
                          final Transaction dto) {
        Transaction previous = get(GUIDs.parse(dto));

        if (Objects.nonEqually(dto.getType(), previous.getType())) {
            throw new ModificationException("Нельзя менять тип транзакции");
        }

        if (Objects.nonEqually(dto.getCash(), previous.getCash())) {
            throw new ModificationException("Нельзя менять сумму транзакции");
        }

        if (Objects.nonEqually(dto.getPerson(), previous.getPerson())) {
            throw new ModificationException("Нельзя менять члена клуба");
        }

        if (Objects.nonEqually(dto.getEvent(), previous.getEvent())) {
            throw new ModificationException("Нельзя менять мероприятие");
        }

        validation(dto);

        repository.update(configuration, dto);
    }

    @Override
    protected void delete(final Configuration configuration, final UUID uuid) {
        Transaction dto = get(uuid);

        BigDecimal money = Numbers.parse(dto.getCash());

        if (dto.getType() == TransactionType.COST) {
            cashbox.contribution(configuration, money);
        }

        if (dto.getType() == TransactionType.CONTRIBUTION) {
            cashbox.cost(configuration, money);
            debt.cost(configuration,
                      GUIDs.parse(dto.getPerson()),
                      GUIDs.parse(dto.getEvent()));
        }

        if (dto.getType() == TransactionType.WRITE_OFF) {
            deposit.contribution(configuration,
                                 GUIDs.parse(dto.getPerson()),
                                 money);
            debt.cost(configuration,
                      GUIDs.parse(dto.getPerson()),
                      GUIDs.parse(dto.getEvent()));
        }

        if (dto.getType() == TransactionType.PAID) {
            deposit.cost(configuration, GUIDs.parse(dto.getPerson()), money);
        }

        repository.delete(configuration, uuid);
    }

    /**
     * Проверка полноты данных.
     *
     * @param dto Транзакция.
     */
    private void validation(final Transaction dto) {
        if (dto.getType() == TransactionType.NONE) {
            throw new ModificationException(
                "У транзакции должен быть указан тип");
        }

        if (dto.getType() != TransactionType.CONTRIBUTION) {
            return;
        }

        if (GUIDs.isEmpty(dto.getPerson())) {
            throw new ModificationException(
                "У взноса должен быть задан пользователь");
        }

        if (GUIDs.nonEmpty(dto.getEvent())) {
            Event e = event.get(GUIDs.parse(dto.getEvent()));
            if (Numbers.nonEqually(e.getContribution(), dto.getCash())) {
                throw new ModificationException(
                     "Внесенный взнос не равен взносу с человека");
            }
        }
    }
}
