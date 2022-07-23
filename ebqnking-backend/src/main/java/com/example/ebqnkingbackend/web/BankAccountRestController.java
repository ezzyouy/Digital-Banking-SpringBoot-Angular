package com.example.ebqnkingbackend.web;

import com.example.ebqnkingbackend.dtos.AccountHistoryDTO;
import com.example.ebqnkingbackend.dtos.AccountOperationDTO;
import com.example.ebqnkingbackend.dtos.BankAccountDTO;
import com.example.ebqnkingbackend.exceptions.BankAccoutNotFoundException;
import com.example.ebqnkingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccS;


    public BankAccountRestController(BankAccountService bankAccS) {
        this.bankAccS = bankAccS;
    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccoutNotFoundException {
        return bankAccS.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccount(){
        return bankAccS.bankAccountList();
    }
    @GetMapping("/account/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccS.accountHistory(accountId);
    }
    @GetMapping("/account/{accountId}/pageOperation")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccoutNotFoundException {
        return bankAccS.getAccountHistory(accountId,page,size);
    }

}
