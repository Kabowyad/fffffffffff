package com.devexperts.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.devexperts.exceptions.AccountNotFoundExceptions;
import com.devexperts.exceptions.AccountValidatingExceptions;
import com.devexperts.exceptions.BalanceNotEnoughException;
import com.devexperts.exceptions.TransferValidationException;
import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import com.devexperts.model.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {

        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {

        Long id = getId(account);
        if (id == null) {
            throw new AccountValidatingExceptions(account.getFirstName(), account.getLastName());
        } else {
            accounts.put(id, account);
        }

    }

    @Override
    public Account getAccount(long id) {

        Account account = accounts.get(id);
        if (account == null) {
            throw new AccountNotFoundExceptions(id);
        }
        return account;
    }

    @Override
    public void transfer(Account source, Account target, double amount) {

        if (amount < Double.MIN_VALUE) {
            throw new TransferValidationException("Amount is negative value");
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new BalanceNotEnoughException(getId(source), source.getBalance());
        }

        int compareId = getId(source).compareTo(getId(target));
        if (compareId < 0) {
            synchronized (source) {
                synchronized (target) {
                    makeTransfer(source, target, amount);
                }
            }
        }
        if (compareId > 0) {
            synchronized (target) {
                synchronized (source) {
                    makeTransfer(source, target, amount);
                }
            }
        }
    }

    private void makeTransfer(Account from, Account to, Double amount) {

        from.setBalance(BigDecimal.valueOf(from.getBalance()).subtract(BigDecimal.valueOf(amount))
                .doubleValue());
        to.setBalance(
                BigDecimal.valueOf(to.getBalance()).add(BigDecimal.valueOf(amount)).doubleValue());
    }

    private Long getId(Account account) {

        try {
            Field field = AccountKey.class.getDeclaredField("accountId");
            field.trySetAccessible();
            return (long) field.get(account.getAccountKey());
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return null;
        }
    }

}
