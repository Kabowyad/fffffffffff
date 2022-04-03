package com.devexperts;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import com.devexperts.model.service.AccountService;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    public AccountService accountService;

    @Test
    public void testCreateAndGetSuccess() {

        Account first = new Account(AccountKey.valueOf(123), "Yan", "Yukh", Double.valueOf("123"));
        accountService.createAccount(first);
        Account res = accountService.getAccount(123);
        Assert.assertEquals(res.getFirstName(), first.getFirstName());
    }

    @Test
    public void testTransferSuccess() {
        Account first = new Account(AccountKey.valueOf(123), "Yan", "Yukh", Double.valueOf("123"));
        Account second = new Account(AccountKey.valueOf(234), "Ivan", "Susanin", Double.valueOf("123"));
        accountService.createAccount(first);
        accountService.createAccount(second);
        accountService.transfer(first, second, Double.parseDouble("123"));
        Assert.assertEquals(accountService.getAccount(123L).getBalance(),
                Double.parseDouble("0"), 0.001);
        Assert.assertEquals(accountService.getAccount(234L).getBalance(),
                Double.parseDouble("246"), 0.001);
    }

}
