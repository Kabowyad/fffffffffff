package com.devexperts.exceptions;

public class BalanceNotEnoughException extends RuntimeException {

    public BalanceNotEnoughException(Long accountId, Double balance) {

        super(String.format("Balance not enough in account %d, with balance", accountId, balance));
    }
}
