package com.roy.expensetracker.service;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.Transaction;
import com.roy.expensetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public List<Transaction> findByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
