package com.cenoa.transactions.repository;

import com.cenoa.transactions.model.Transfer;
import com.cenoa.transactions.model.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

}
