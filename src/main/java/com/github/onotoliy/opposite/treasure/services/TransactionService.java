package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.repositories.CashboxRepository;
import com.github.onotoliy.opposite.treasure.repositories.DepositRepository;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.repositories.TransactionRepository;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;
import static com.github.onotoliy.opposite.treasure.utils.ObjectUtil.OBJECT;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

@Service
public class TransactionService
extends AbstractModifierService<
    Transaction,
    TransactionSearchParameter,
    TransactionRepository> {

    private final CashboxRepository cashbox;

    private final DepositRepository deposit;

    private final EventRepository event;

    @Autowired
    public TransactionService(TransactionRepository repository,
                              CashboxRepository cashbox,
                              DepositRepository deposit,
                              EventRepository event) {
        super(repository);
        this.cashbox = cashbox;
        this.deposit = deposit;
        this.event = event;
    }

    @Override
    protected void create(Configuration configuration, Transaction dto) {
        BigDecimal money = MONEY.parse(dto.getCash());

        validation(dto);

        if (dto.getType() == TransactionType.CONTRIBUTION) {
            cashbox.contribution(configuration, money);

            if (Objects.nonNull(dto.getPerson())) {
                deposit.contribution(
                    configuration, GUID.parse(dto.getPerson()), money);
            }
        } else {
            cashbox.cost(configuration, money);
        }

        repository.create(configuration, dto);
    }

    @Override
    protected void update(Configuration configuration, Transaction dto) {
        Transaction previous = get(GUID.parse(dto));

        if (OBJECT.nonEqually(dto.getType(), previous.getType())) {
            throw new ModificationException("Нельзя менять тип транзакции");
        }

        if (OBJECT.nonEqually(dto.getCash(), previous.getCash())) {
            throw new ModificationException("Нельзя менять сумму взносов/расходов");
        }

        validation(dto);

        if (GUID.notEqually(previous.getPerson(), dto.getPerson())) {
            if (previous.getType() == TransactionType.CONTRIBUTION) {
                BigDecimal money = MONEY.parse(dto.getCash());

                deposit.contribution(configuration, GUID.parse(dto.getPerson()), money);
                deposit.cost(configuration, GUID.parse(previous.getPerson()), money);
            }
        }

        repository.update(configuration, dto);
    }

    @Override
    protected void delete(Configuration configuration, UUID uuid) {
        Transaction dto = get(uuid);

        BigDecimal money = MONEY.parse(dto.getCash());

        if (dto.getType() == TransactionType.CONTRIBUTION) {
            cashbox.cost(configuration, money);

            if (Objects.nonNull(dto.getPerson())) {
                deposit.cost(configuration, GUID.parse(dto.getPerson()), money);
            }
        } else {
            cashbox.contribution(configuration, money);
        }

        repository.delete(configuration, uuid);
    }

    private void validation(Transaction dto) {
        if (dto.getType() != TransactionType.CONTRIBUTION) {
            return;
        }

        if (GUID.isEmpty(dto.getPerson())) {
            throw new ModificationException("У взноса должен быть задан пользователь");
        }

        if (GUID.nonEmpty(dto.getEvent())) {
            Event event = this.event.get(GUID.parse(dto.getEvent()));
            if (MONEY.nonEqually(event.getContribution(), dto.getCash())) {
                throw new ModificationException(
                    "Внесенный взнос не равен взносу с человека");
            }
        }
    }
}
