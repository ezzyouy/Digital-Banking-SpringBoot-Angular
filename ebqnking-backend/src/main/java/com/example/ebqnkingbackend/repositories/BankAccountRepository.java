package com.example.ebqnkingbackend.repositories;

import com.example.ebqnkingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
