package com.example.ebqnkingbackend;

import com.example.ebqnkingbackend.dtos.BankAccountDTO;
import com.example.ebqnkingbackend.dtos.CurrentBankAccountDTO;
import com.example.ebqnkingbackend.dtos.CustmerDTO;
import com.example.ebqnkingbackend.dtos.SavingBankAccountDTO;
import com.example.ebqnkingbackend.entities.*;
import com.example.ebqnkingbackend.enums.AccountStatus;
import com.example.ebqnkingbackend.enums.OperationType;
import com.example.ebqnkingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebqnkingbackend.exceptions.BankAccoutNotFoundException;
import com.example.ebqnkingbackend.exceptions.CustomerNotFoundException;
import com.example.ebqnkingbackend.repositories.AccountOperationRepository;
import com.example.ebqnkingbackend.repositories.BankAccountRepository;
import com.example.ebqnkingbackend.repositories.CustomerRepository;
import com.example.ebqnkingbackend.services.BankAccountService;
import com.example.ebqnkingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbqnkingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbqnkingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner start(BankAccountService bas) {
        return args -> {
                Stream.of("Imane", "Mohamed", "Hassan").forEach(name->{
                    CustmerDTO custmer=new CustmerDTO();
                    custmer.setName(name);
                    custmer.setEmail(name+"@gmail.com");
                    bas.saveCustomer(custmer);
                });
                bas.listCustomer().forEach(cust->{
                    try {
                        bas.saveCurrentBankAccount(Math.random()*90000, 9000, cust.getId());
                        bas.saveSavingBankAccount(Math.random()*120000,5.5,cust.getId());

                    } catch (CustomerNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            List<BankAccountDTO> bankAccounts=bas.bankAccountList();
            for (BankAccountDTO  bankAcc:bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if(bankAcc instanceof SavingBankAccountDTO){
                        accountId=((SavingBankAccountDTO) bankAcc).getId();
                    }else {
                        accountId=((CurrentBankAccountDTO) bankAcc).getId();
                    }
                    bas.credit(accountId,10000+Math.random()*120000,"Credit");
                    bas.debit(accountId,1000+Math.random()*9000,"Debit" );
                }
            }
        };
    }
   // @Bean
    CommandLineRunner start(CustomerRepository cr, BankAccountRepository bar, AccountOperationRepository aor){
        return  args -> {
            Stream.of("Hassan", "Yassin","Aicha").forEach(name->{
                Custmer custmer=new Custmer();
                custmer.setName(name);
                custmer.setEmail(name+"@gmail.com");
                cr.save(custmer);
            });
            cr.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustmer(cust);
                currentAccount.setOverDraft(9000);
                bar.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustmer(cust);
                savingAccount.setInterestRate(5.5);
                bar.save(savingAccount);
            });
            bar.findAll().forEach(acc->{
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    aor.save(accountOperation);
                }

            });
        };

    }
}
