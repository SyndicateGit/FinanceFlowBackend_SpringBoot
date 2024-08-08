package com.financeflow.financeflow_backend.dto;

import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.RecurringType;
import com.financeflow.financeflow_backend.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime date;
    private boolean isRecurring;
    private RecurringType recurringType;
    private String note;
    private String category;
    private String description;
    private Long linkedTransactionId;
}
