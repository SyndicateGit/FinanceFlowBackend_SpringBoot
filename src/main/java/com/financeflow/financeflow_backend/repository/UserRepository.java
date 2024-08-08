package com.financeflow.financeflow_backend.repository;

import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountsContaining(Account account);
}
