package com.roy.expensetracker.service;

import com.roy.expensetracker.entity.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAccountsOfUser(String username);
    void saveAccount(Account account);
    Account findById(long accountId);
    void deleteAccount(long accountId);
    List<Account> calculateBalanceFromAccounts(List<Account> accounts);
}
