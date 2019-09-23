package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

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
        return user.find(GUID.parse("8098207c-6fbb-45ec-ae10-184607262a1a"));
    }

    @GetMapping(value = "/list")
    public List<Option> getAll() {
        return user.getAll();
    }
}
