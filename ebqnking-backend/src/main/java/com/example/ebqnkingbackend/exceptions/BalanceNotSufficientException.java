package com.example.ebqnkingbackend.exceptions;

public class BalanceNotSufficientException extends  Exception {
    public BalanceNotSufficientException(String msg) {
        super(msg);
    }
}
