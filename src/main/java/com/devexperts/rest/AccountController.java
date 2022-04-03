package com.devexperts.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devexperts.model.account.Account;
import com.devexperts.model.rest.AbstractAccountController;
import com.devexperts.model.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController implements AbstractAccountController {

    private final AccountService accountService;

    @PostMapping("/operations/transfer/{sourceId}/{targetId}/{amount}")
    public ResponseEntity<Void> transfer(@PathVariable("sourceId") long sourceId,
            @PathVariable("targetId") long targetId, @PathVariable("amount") double amount) {

        Account source = accountService.getAccount(sourceId);
        Account target = accountService.getAccount(targetId);
        accountService.transfer(source, target, amount);
        return ResponseEntity.ok().build();
    }
}
