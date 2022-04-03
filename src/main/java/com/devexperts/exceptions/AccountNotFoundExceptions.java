package com.devexperts.exceptions;

public class AccountNotFoundExceptions extends RuntimeException {

    public AccountNotFoundExceptions(Long id) {
        super(String.format("Account with id %d not found", id));
    }

}
