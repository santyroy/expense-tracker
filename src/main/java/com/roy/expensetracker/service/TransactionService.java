package com.roy.expensetracker.service;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findByAccount(Account account);
    void addTransaction(Transaction transaction);
}
