package com.example.ebqnkingbackend.dtos;


import com.example.ebqnkingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;


@Data
public class CurrentBankAccountDTO extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustmerDTO custmerDTO;
    private double overDraft;
}
