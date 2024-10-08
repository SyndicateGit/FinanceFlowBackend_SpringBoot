package com.financeflow.financeflow_backend.entity;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // Specifies class as JPA entity
@Table(name = "transactions") // Specifies the name of the table in the database autogenerated by Hibernate
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;
    private String currency;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "is_recurring")
    private boolean isRecurring;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_type")
    private RecurringType recurringType;

    @Column(nullable = false)
    private String note;
    @Column(nullable = false)
    private String category;
    private String description;

    @Column(name = "linked_transaction_id")
    private Long linkedTransactionId;

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void applyTransaction(Account account){
        if(this.type.equals(TransactionType.TRANSFER)){
            throw new IllegalArgumentException(
                    "Use applyTransaction(Account fromAccount, Account toAccount) for transfer transactions"
            );
        }
        if(this.type.equals(TransactionType.INCOME)){
            account.deposit(this.amount);
        } else {
            account.withdraw(this.amount);
        }
        account.addTransaction(this);
    }

    public void revertTransfer(Account fromAccount, Account toAccount){
        if(!this.type.equals(TransactionType.TRANSFER)){
            throw new IllegalArgumentException(
                    "Use revertTransfer() only for transfer transactions"
            );
        }
        fromAccount.deposit(this.amount);
        toAccount.withdraw(this.amount);
    }
}
