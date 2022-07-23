package com.example.ebqnkingbackend.services;

import com.example.ebqnkingbackend.entities.BankAccount;
import com.example.ebqnkingbackend.entities.CurrentAccount;
import com.example.ebqnkingbackend.entities.SavingAccount;
import com.example.ebqnkingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bar;

    public void consulter(){
        BankAccount bankAccount=bar.findById("11687447-5f0f-4ea5-91f8-27dab97fda72").orElse(null);
        if(bankAccount!=null){
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustmer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if(bankAccount instanceof CurrentAccount){
                System.out.println("Over Draft :"+((CurrentAccount)bankAccount).getOverDraft());;
            }else if(bankAccount instanceof SavingAccount){
                System.out.println("Rate :"+((SavingAccount)bankAccount).getInterestRate());;
            }
            bankAccount.getAccountOperation().forEach(op->{
                System.out.println(op.getType()+"\t"+op.getOperationDate()+"\t"+op.getAmount());

            });
        }
    }
}
