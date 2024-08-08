package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.TransactionDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.Transaction;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.TransactionMapper;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.TransactionRepository;
import com.financeflow.financeflow_backend.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long accountId) {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);

        account.addTransaction(savedTransaction);
        accountRepository.save(account);

        return TransactionMapper.mapToTransactionDTO(savedTransaction);
    }
}
