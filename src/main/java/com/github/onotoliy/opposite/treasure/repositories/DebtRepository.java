package com.github.onotoliy.opposite.treasure.repositories;

import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEBT;

@Repository
public class DebtRepository {

    private final DSLContext dsl;

    @Autowired
    public DebtRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void cost(Configuration configuration, UUID person, UUID event) {
        DSL.using(configuration)
           .insertInto(TREASURE_DEBT)
           .set(TREASURE_DEBT.USER_UUID, person)
           .set(TREASURE_DEBT.EVENT_GUID, event)
           .execute();
    }

    public void contribution(Configuration configuration, UUID person, UUID event) {
        DSL.using(configuration)
           .deleteFrom(TREASURE_DEBT)
           .where(
               TREASURE_DEBT.USER_UUID.eq(person),
               TREASURE_DEBT.EVENT_GUID.eq(event))
           .execute();
    }
}
