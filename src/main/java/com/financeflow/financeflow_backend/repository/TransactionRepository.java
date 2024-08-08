package com.financeflow.financeflow_backend.repository;

import com.financeflow.financeflow_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
