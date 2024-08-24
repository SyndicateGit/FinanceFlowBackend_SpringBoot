package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;
public interface AccountService {
    AccountDTO getAccountById(Long id);
    List<AccountDTO> getAccountsByUserId(Long userId);
    List<AccountDTO> getAllAccounts();

    AccountDTO createAccount(AccountDTO accountDTO, Long bankId, Long userId);
    AccountDTO updateAccount(Long id, AccountDTO accountDTO);
    void deleteAccount(Long id);

    BigDecimal deposit(Long accountId, BigDecimal amount);
    BigDecimal withdrawal(Long accountId, BigDecimal amount);
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    List<AccountDTO> getUserAcounts();
}
