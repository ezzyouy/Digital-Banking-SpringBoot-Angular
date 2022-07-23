package com.example.ebqnkingbackend.repositories;

import com.example.ebqnkingbackend.entities.Custmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Custmer, Long> {
    @Query("select c from Custmer c where c.name like :kw")
    List<Custmer> searchCustmer(@Param("kw") String keyword);
}
