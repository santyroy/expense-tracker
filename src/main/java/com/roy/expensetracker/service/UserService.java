package com.roy.expensetracker.service;

import com.roy.expensetracker.entity.User;

public interface UserService {

    User findByUsername(String username);
    boolean addUser(User user);
}
