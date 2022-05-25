package com.roy.expensetracker.controller;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.Transaction;
import com.roy.expensetracker.service.AccountService;
import com.roy.expensetracker.service.TransactionService;
import com.roy.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/v1/report")
public class ReportController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @GetMapping
    public String showReportByAccount(Principal principal, ModelMap model) {
        // get original account details
        List<Account> originalAccountDetails = accountService.getAccountsOfUser(principal.getName());

        // calculate balance from accounts - for UI update
        List<Account> modifiedAccountDetails = accountService.calculateBalanceFromAccounts(originalAccountDetails);

        model.addAttribute("pieData", getPieData(modifiedAccountDetails));
        return "report_overall";
    }

    private List<List<Object>> getPieData(List<Account> accounts) {
        List<List<Object>> objectList = new ArrayList<>();
        objectList.add(List.of("Account", "Balance"));
        for (Account account : accounts) {
            objectList.add(List.of(account.getName(), account.getBalance()));
        }
        return objectList;
    }

    @GetMapping("{accountId}")
    public String showReportByDebitInTransaction(Principal principal, @PathVariable("accountId") int accountId, ModelMap model) {
        Account account = accountService.findById(principal, accountId);

        model.addAttribute("chartData", getChartData(account));
        model.addAttribute("account", account != null ? account.getName() : "not found");
        return "report";
    }

    private List<List<Object>> getChartData(Account account) {
        List<Transaction> transactions = transactionService.findByAccount(account);

//        return transactions.stream().map(transaction -> List.of(transaction.getTime().toLocalDate(), transaction.getAmount())).collect(Collectors.toList());

        List<List<Object>> objectList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if(transaction.getType().equalsIgnoreCase("debit")){
                objectList.add(List.of(transaction.getTime().toLocalDate(), transaction.getAmount()));
            }
        }
        return objectList;

//        return List.of(
//                List.of(transactions.get(0).getTime().toLocalDate(), transactions.get(0).getAmount()),
//                List.of(transactions.get(1).getTime().toLocalDate(), transactions.get(1).getAmount()),
//                List.of(transactions.get(2).getTime().toLocalDate(), transactions.get(2).getAmount()),
//                List.of(transactions.get(3).getTime().toLocalDate(), transactions.get(3).getAmount()),
//                List.of(transactions.get(4).getTime().toLocalDate(), transactions.get(4).getAmount())
//        );
    }

//    private List<List<Object>> getChartData() {
//        return List.of(
//                List.of("Mushrooms", RANDOM.nextInt(5)),
//                List.of("Onions", RANDOM.nextInt(5)),
//                List.of("Olives", RANDOM.nextInt(5)),
//                List.of("Zucchini", RANDOM.nextInt(5)),
//                List.of("Pepperoni", RANDOM.nextInt(5))
//        );
//    }
}
