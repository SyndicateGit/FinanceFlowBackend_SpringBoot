package com.financeflow.financeflow_backend.repository;

import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByTransactionsContaining(Transaction transaction);
}
