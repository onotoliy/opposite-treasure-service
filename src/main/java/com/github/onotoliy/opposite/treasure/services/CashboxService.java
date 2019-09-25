package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.treasure.repositories.CashboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashboxService {

    public final CashboxRepository repository;

    @Autowired
    public CashboxService(final CashboxRepository repository) {
        this.repository = repository;
    }

    public Cashbox get() {
        return repository.find();
    }
}
