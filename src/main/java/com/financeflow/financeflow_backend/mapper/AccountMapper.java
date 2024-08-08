package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.AccountDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.entity.Transaction;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.TransactionRepository;
import com.financeflow.financeflow_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AccountMapper {
    private static AccountRepository accountRepository;
    private static UserRepository userRepository;
    @Autowired
    public AccountMapper(AccountRepository accountRepository, UserRepository userRepository) {
        AccountMapper.accountRepository = accountRepository;
        AccountMapper.userRepository = userRepository;
    }

    public static AccountDTO mapToAccountDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getTransactions().stream().map(Transaction::getId).toList()
        );
    }

    public static Account mapToAccount(AccountDTO accountDTO) {

        return new Account(
                accountDTO.getId(),
                accountDTO.getAccountType(),
                accountDTO.getBalance(),
                accountDTO.getCurrency(),
                Collections.emptyList() // Empty list of transactions
        );
    }
}
