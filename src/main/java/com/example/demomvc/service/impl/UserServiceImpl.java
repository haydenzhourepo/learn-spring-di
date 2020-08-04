package com.example.demomvc.service.impl;

import com.example.demomvc.annotation.ZAutowire;
import com.example.demomvc.annotation.ZService;
import com.example.demomvc.service.IUserService;
import com.example.demomvc.service.UserRepository;

/**
 * @author hayden zhou
 * @date 2020/8/4 8:31 上午
 */
@ZService("userServiceA")
public class UserServiceImpl implements IUserService {

    @ZAutowire
    public UserRepository userRepository;

    @Override
    public String createUser(String name) {
        return userRepository.createUser(name);
    }
}
