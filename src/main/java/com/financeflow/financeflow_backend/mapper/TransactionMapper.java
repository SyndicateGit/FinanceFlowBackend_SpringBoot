package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.TransactionDTO;
import com.financeflow.financeflow_backend.entity.Transaction;
import org.springframework.stereotype.Component;
import java.util.UUID;
import static com.financeflow.financeflow_backend.mapper.AccountMapper.mapToAccountDTO;

@Component
public class TransactionMapper {
    public static TransactionDTO mapToTransactionDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDate(),
                transaction.isRecurring(),
                transaction.getRecurringType(),
                transaction.getNote(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getLinkedTransactionId()
        );
    }

    public static Transaction mapToTransaction(TransactionDTO transactionDTO){
        return new Transaction(
                transactionDTO.getId(),
                transactionDTO.getType(),
                transactionDTO.getAmount(),
                transactionDTO.getCurrency(),
                transactionDTO.getDate(),
                transactionDTO.isRecurring(),
                transactionDTO.getRecurringType(),
                transactionDTO.getNote(),
                transactionDTO.getCategory(),
                transactionDTO.getDescription(),
                transactionDTO.getLinkedTransactionId()
                );
    }
}
