package com.roy.expensetracker.controller;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.Transaction;
import com.roy.expensetracker.service.AccountService;
import com.roy.expensetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/v1/account/{accountId}/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @GetMapping
    public String showTransactionList(Principal principal, ModelMap modelMap, @PathVariable long accountId) {

        Account account = accountService.findById(principal, accountId);

        if (account == null) {
            modelMap.put("accountId", false);
            return "transactions";
        }
        List<Transaction> transactions = transactionService.findByAccount(account);

        modelMap.put("name", principal.getName());
        modelMap.put("transactions", transactions);
        modelMap.put("accountId", accountId);
        return "transactions";
    }

    @GetMapping("/add")
    public String showTransactionFrom(Principal principal, @PathVariable long accountId, ModelMap modelMap) {
        Account account = accountService.findById(principal, accountId);

        if (account == null) {
            modelMap.put("accountId", false);
            return "transactions";
        }
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTime(LocalDateTime.now());

        modelMap.put("accountId", accountId);
        modelMap.put("transaction", transaction);

        return "transaction_form";
    }

    @PostMapping
    public String addTransaction(@Valid Transaction transaction, Errors errors, @PathVariable long accountId) {
        if (errors.getErrorCount() == 0) {
            transactionService.addTransaction(transaction);
            return "redirect:/v1/account/{accountId}/transaction";
        }
        return "transaction_form";
    }
}
