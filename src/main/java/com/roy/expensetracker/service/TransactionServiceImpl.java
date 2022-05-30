package com.roy.expensetracker.service;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.Transaction;
import com.roy.expensetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public List<Transaction> findByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Override
    public void addTransaction(Principal principal, long accountId, Transaction transaction) {
        Account account = accountService.findById(principal, accountId);
        float oldBalance = account.getBalance();
        if (transaction.getType().equalsIgnoreCase("credit")) {
            account.setBalance(oldBalance + transaction.getAmount());
        } else {
            account.setBalance(oldBalance - transaction.getAmount());
        }
        accountService.saveAccount(account);
        transactionRepository.save(transaction);
    }
}
