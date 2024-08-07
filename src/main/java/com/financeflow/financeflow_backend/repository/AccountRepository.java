package com.financeflow.financeflow_backend.repository;

import com.financeflow.financeflow_backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUserId(Long userId);
}
