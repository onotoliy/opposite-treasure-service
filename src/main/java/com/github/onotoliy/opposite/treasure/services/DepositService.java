package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepositService {

    private final DepositRepository repository;

    @Autowired
    public DepositService(DepositRepository repository) {
        this.repository = repository;
    }

    public Deposit get(UUID uuid) {
        return repository.get(uuid);
    }

    public Page<Deposit> getAll(DepositSearchParameter parameter) {
        return repository.getAll(parameter);
    }
}
