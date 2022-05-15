package com.roy.expensetracker.controller;

import com.roy.expensetracker.entity.User;
import com.roy.expensetracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/v1/register")
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationPage(ModelMap modelMap) {
        modelMap.put("user", new User());
        return "register";
    }

    @PostMapping
    public String addUser(@Valid User user, Errors errors, ModelMap modelMap) {

        if (errors.getErrorCount() == 0) {
            log.info("Adding new user: " + user.getUsername());
            boolean isUserAdded = userService.addUser(user);

            if (isUserAdded) {
                return "redirect:/v1/login";
            } else {
                log.info("user: " + user.getUsername() + " already exists");
                modelMap.put("error", "Email already taken");
                return "register";
            }
        } else {
            return "register";
        }
    }
}
