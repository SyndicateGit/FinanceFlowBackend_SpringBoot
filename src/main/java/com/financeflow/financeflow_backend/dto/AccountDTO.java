package com.financeflow.financeflow_backend.dto;

import com.financeflow.financeflow_backend.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;

    private String accountHolderName;

    private Account.AccountType accountType;

    private BigDecimal balance;
    private String currency;
}
