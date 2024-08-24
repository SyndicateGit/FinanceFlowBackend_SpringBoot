package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.BankDTO;

import com.financeflow.financeflow_backend.entity.Bank;
import com.financeflow.financeflow_backend.entity.Account;

public class BankMapper {
    public static BankDTO mapToBankDTO(Bank bank){
        return new BankDTO(
                bank.getId(),
                bank.getName(),
                bank.getAccounts().stream().map(Account::getId).toList()
        );
    }
}
