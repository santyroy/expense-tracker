package com.roy.expensetracker.service;

import com.roy.expensetracker.entity.Account;
import com.roy.expensetracker.entity.Transaction;
import com.roy.expensetracker.entity.User;
import com.roy.expensetracker.repository.AccountRepository;
import com.roy.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAccountsOfUser(String username) {
        // Get the user id
        User user = userRepository.findByUsername(username);

        // fetch the accounts for the user
        return accountRepository.findByUser(user);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findById(long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    @Override
    public void deleteAccount(long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<Account> calculateBalanceFromAccounts(List<Account> originalAccounts) {
        List<Account> accountList = new ArrayList<>(originalAccounts);
        List<Account> finalAccountList = new ArrayList<>();
        for (Account account : accountList) {
            float originalBalance = account.getBalance();
            if (account.getTransactions() != null || account.getTransactions().size() > 0) {
                float currentBalance = calculateBalance(originalBalance, account.getTransactions());
                account.setBalance(currentBalance);
                finalAccountList.add(account);
            } else {
                finalAccountList.add(account);
            }
        }

        return finalAccountList;
    }

    private float calculateBalance(float originalBalance, List<Transaction> transactions) {
        AtomicReference<Float> currentBalance = new AtomicReference<>(originalBalance);
        transactions.forEach(transaction -> {
            if ("debit".equalsIgnoreCase(transaction.getType())) {
                currentBalance.updateAndGet(v -> (v - transaction.getAmount()));
            } else {
                currentBalance.updateAndGet(v -> (v + transaction.getAmount()));
            }
        });
        return currentBalance.get();
    }
}
