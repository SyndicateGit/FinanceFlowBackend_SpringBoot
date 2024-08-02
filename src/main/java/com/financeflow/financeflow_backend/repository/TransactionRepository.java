package com.financeflow.financeflow_backend.repository;

import com.financeflow.financeflow_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
