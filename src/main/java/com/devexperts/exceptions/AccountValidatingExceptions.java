package com.devexperts.exceptions;

public class AccountValidatingExceptions extends RuntimeException {

    public AccountValidatingExceptions(String firstName, String lastName) {

        super(String.format("Trying to add account %s %s with empty id", firstName, lastName));
    }
}
