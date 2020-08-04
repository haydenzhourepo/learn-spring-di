package com.example.demomvc.controller;

import com.example.demomvc.annotation.ZAutowire;
import com.example.demomvc.core.Container;
import com.example.demomvc.service.IUserService;
import com.sun.org.apache.bcel.internal.generic.IUSHR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hayden zhou
 * @date 2020/8/3 9:32 下午
 */
@RequestMapping("user")
@RestController
public class UserConroller {


    @GetMapping("{name}")
    public String createUser(@PathVariable String name) {
        IUserService userService = ((IUserService) Container.getContainer().get("com.example.demomvc.service.IUserService"));
        return userService.createUser(name);
    }
}
