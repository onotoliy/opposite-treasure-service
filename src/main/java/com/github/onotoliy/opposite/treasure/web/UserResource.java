package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserResource {

    private final UserRPC user;

    @Autowired
    public UserResource(UserRPC user) {
        this.user = user;
    }

    @GetMapping(value = "/current")
    public Option getCurrentUser() {
        return user.getAll().get(0);
    }

    @GetMapping(value = "/list")
    public List<Option> getAll() {
        return user.getAll();
    }
}
