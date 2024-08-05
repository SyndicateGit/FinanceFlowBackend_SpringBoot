package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.AccountDTO;
import com.financeflow.financeflow_backend.entity.Account;

public class AccountMapper {
    public static AccountDTO mapToAccountDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountHolderName(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency()
        );
    }

    public static Account mapToAccount(AccountDTO accountDTO) {
        return new Account(
                accountDTO.getId(),
                accountDTO.getAccountType(),
                accountDTO.getBalance(),
                accountDTO.getCurrency(),
                null
        );
    }
}
