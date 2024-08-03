package com.financeflow.financeflow_backend.dto;

import com.financeflow.financeflow_backend.model.Transaction;
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
    private Long accountId;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime date;
    private boolean isRecurring;
    private Transaction.RecurringType recurringType;
    private String note;
    private String category;
    private String description;
}
