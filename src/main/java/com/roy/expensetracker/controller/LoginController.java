package com.roy.expensetracker.controller;

import com.roy.expensetracker.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/login")
public class LoginController {

    @GetMapping
    public String showLoginPage(ModelMap modelMap) {
        modelMap.put("user", new User());
        return "login";
    }
}
