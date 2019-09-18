package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.treasure.services.CashboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cashbox")
public class CashboxResource {

    private final CashboxService service;

    @Autowired
    public CashboxResource(CashboxService service) {
        this.service = service;
    }

    @GetMapping
    public Cashbox get() {
        return service.get();
    }
}
