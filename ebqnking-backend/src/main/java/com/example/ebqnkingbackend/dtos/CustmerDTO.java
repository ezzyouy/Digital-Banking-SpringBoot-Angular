package com.example.ebqnkingbackend.dtos;

import com.example.ebqnkingbackend.entities.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
public class CustmerDTO {
    private Long id;
    private String name;
    private String email;
}
