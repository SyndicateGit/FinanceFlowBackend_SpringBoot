package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.BankDTO;

import com.financeflow.financeflow_backend.entity.Bank;
import com.financeflow.financeflow_backend.entity.Account;

import java.util.Collections;

public class BankMapper {
    public static BankDTO mapToBankDTO(Bank bank){
        return new BankDTO(
                bank.getId(),
                bank.getName(),
                bank.getTotalBalance()
        );
    }

    public static Bank mapToBank(BankDTO bankDTO){
        return new Bank(
                bankDTO.getId(),
                bankDTO.getName(),
                Collections.emptyList()
        );
    }
}
