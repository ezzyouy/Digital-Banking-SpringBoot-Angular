package com.example.ebqnkingbackend.mappers;

import com.example.ebqnkingbackend.dtos.AccountOperationDTO;
import com.example.ebqnkingbackend.dtos.CurrentBankAccountDTO;
import com.example.ebqnkingbackend.dtos.CustmerDTO;
import com.example.ebqnkingbackend.dtos.SavingBankAccountDTO;
import com.example.ebqnkingbackend.entities.AccountOperation;
import com.example.ebqnkingbackend.entities.CurrentAccount;
import com.example.ebqnkingbackend.entities.Custmer;
import com.example.ebqnkingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustmerDTO fromCustomer(Custmer custmer){
        CustmerDTO custmerDTO=new CustmerDTO();
        BeanUtils.copyProperties(custmer,custmerDTO);
        /*custmerDTO.setId(custmer.getId());
        custmerDTO.setName(custmer.getName());
        custmerDTO.setEmail(custmer.getEmail());*/
        return custmerDTO;
    }
    public Custmer fromCustomerDTO(CustmerDTO custmerDTO){
        Custmer custmer=new Custmer();
        BeanUtils.copyProperties(custmerDTO,custmer);
        return  custmer;
    }
    public SavingBankAccountDTO fromSavingAccount(SavingAccount sAcc){
        SavingBankAccountDTO savingAccDTO=new SavingBankAccountDTO();
        BeanUtils.copyProperties(sAcc,savingAccDTO);
        savingAccDTO.setCustmerDTO(fromCustomer(sAcc.getCustmer()));
        savingAccDTO.setType(sAcc.getClass().getSimpleName());
        return  savingAccDTO;
    }

    public SavingAccount FromCurrentBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustmer(fromCustomerDTO(savingBankAccountDTO.getCustmerDTO()));
        return savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount c){
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(c,currentBankAccountDTO);
        currentBankAccountDTO.setCustmerDTO(fromCustomer(c.getCustmer()));
        currentBankAccountDTO.setType(c.getClass().getSimpleName());
        return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentAccountDTO(CurrentBankAccountDTO cDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(cDTO,currentAccount);
        currentAccount.setCustmer(fromCustomerDTO(cDTO.getCustmerDTO()));
        return currentAccount;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return  accountOperationDTO;
    }

}
