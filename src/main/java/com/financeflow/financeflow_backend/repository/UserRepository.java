package com.financeflow.financeflow_backend.repository;

import com.financeflow.financeflow_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
