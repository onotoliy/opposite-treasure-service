package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.services.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/deposit")
public class DepositResource{

    private final DepositService service;

    @Autowired
    public DepositResource(DepositService service) {
        this.service = service;
    }

    @GetMapping(value = "/{uuid}")
    public Deposit get(@PathVariable("uuid") UUID uuid) {
        return service.get(uuid);
    }

    @GetMapping
    public Page<Deposit> getAll(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "numberOfRows", required = false, defaultValue = "10") int numberOfRows) {
        return service.getAll(new DepositSearchParameter(offset, numberOfRows));
    }
}
