package com.example.ebqnkingbackend.services;


import com.example.ebqnkingbackend.dtos.*;
import com.example.ebqnkingbackend.entities.BankAccount;
import com.example.ebqnkingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebqnkingbackend.exceptions.BankAccoutNotFoundException;
import com.example.ebqnkingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustmerDTO saveCustomer(CustmerDTO c);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDrift, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustmerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccoutNotFoundException;
    void debit(String accountId, double amount,String description) throws BalanceNotSufficientException, BankAccoutNotFoundException;
    void credit(String accountId, double amount,String description) throws BankAccoutNotFoundException, BalanceNotSufficientException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccoutNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustmerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CustmerDTO updateCustomer(CustmerDTO custmerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccoutNotFoundException;

    List<CustmerDTO> searchCustomers(String keyword);
}
