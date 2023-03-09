package com.cenoa.transactions.repository;

import com.cenoa.transactions.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {

}
