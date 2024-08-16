package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.AccountDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.AccountMapper;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    @Override
    public AccountDTO createAccount(AccountDTO accountDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Account account = AccountMapper.mapToAccount(accountDTO);
        Account savedAccount = accountRepository.save(account);
        User updatedUser = user.addAccount(savedAccount);
        userRepository.save(updatedUser);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return AccountMapper.mapToAccountDTO(account);
    }

    @Override
    public List<AccountDTO> getAccountsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Account> accounts = user.getAccounts();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDTO)
                .collect(Collectors.toList());
    }
    @Override
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(accountDTO.getCurrency());
        Account updatedAccount = accountRepository.save(account);
        // Don't update transactions.
        // Responsibility lies with transaction service
        return AccountMapper.mapToAccountDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        User user = userRepository.findByAccountsContaining(account)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Remove account from user cascades to remove account since orphanRemoval is true
        // Deleting user also deletes account
        user.getAccounts().remove(account);
        userRepository.save(user);
    }

    @Override
    public BigDecimal deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.deposit(amount);
        Account SavedAccount = accountRepository.save(account);
        return SavedAccount.getBalance();
    }

    @Override
    public BigDecimal withdrawal(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.withdraw(amount);
        Account savedAccount = accountRepository.save(account);
        return savedAccount.getBalance();
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        fromAccount.transfer(toAccount, amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Override
    public List<AccountDTO> getUserAcounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Account> accounts = user.getAccounts();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDTO)
                .collect(Collectors.toList());
    }


}
