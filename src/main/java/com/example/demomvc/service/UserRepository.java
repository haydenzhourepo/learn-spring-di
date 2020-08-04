package com.example.demomvc.service;

import com.example.demomvc.annotation.ZRepository;

/**
 * @author hayden zhou
 * @date 2020/8/4 9:33 上午
 */
// 为了扫描包方便 直接把repository 放到service 目录下 一起扫描 简化一下项目
@ZRepository
public class UserRepository {

    public String createUser(String name) {
        return "user: " + name + " created...";
    }
}
