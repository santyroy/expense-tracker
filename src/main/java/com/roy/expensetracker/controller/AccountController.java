package com.roy.expensetracker.controller;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.User;
import com.roy.expensetracker.service.AccountService;
import com.roy.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAccounts(Principal principal, ModelMap modelMap) {
        List<Account> originalAccountDetails = accountService.getAccountsOfUser(principal.getName());

        // calculate balance from accounts - for UI update
        List<Account> modifiedAccountDetails = accountService.calculateBalanceFromAccounts(originalAccountDetails);

        modelMap.put("name", principal.getName());
        modelMap.put("accounts", modifiedAccountDetails);
        return "account_overview";
    }

    @GetMapping("/add")
    public String addAccount(ModelMap modelMap, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        Account account = new Account();
        account.setTime(LocalDateTime.now());
        account.setUser(user);

        modelMap.put("account", account);
        return "add_account";
    }

    @PostMapping
    public String saveAccount(@Valid Account account, Errors errors) {
        if (errors.getErrorCount() == 0) {
            accountService.saveAccount(account);
            return "redirect:/v1/account";
        }
        return "add_account";
    }

    @PostMapping("{accountId}")
    public String deleteAccount(@PathVariable long accountId, ModelMap modelMap) {
        accountService.deleteAccount(accountId);
        return "redirect:/v1/account";
    }
}
