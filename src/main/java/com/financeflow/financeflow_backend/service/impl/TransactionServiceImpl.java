package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.TransactionDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.Transaction;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.TransactionMapper;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.TransactionRepository;
import com.financeflow.financeflow_backend.service.TransactionService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private EntityManager entityManager;
    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long accountId) {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);

        try{
            Transaction savedTransaction = transactionRepository.save(transaction);
            savedTransaction.applyTransaction(account);
            accountRepository.save(account);
            return TransactionMapper.mapToTransactionDTO(savedTransaction);
        } catch (Exception e){
            throw new ResourceNotFoundException("Transaction not saved");
        }
    }

    @Override
    public String createTransferTransaction(TransactionDTO transactionDTO, Long from_account_id, Long to_account_id) {
        Account from_account = accountRepository
                .findById(from_account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Account to_account = accountRepository
                .findById(to_account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Transaction to_transaction = TransactionMapper.mapToTransaction(transactionDTO);
        Transaction from_transaction = TransactionMapper.mapToTransaction(transactionDTO);
        try{

            from_account.transfer(to_account, from_transaction.getAmount());

            from_account.addTransaction(from_transaction);
            to_account.addTransaction(to_transaction);

            accountRepository.save(from_account);
            accountRepository.save(to_account);

            return "Transfer saved";
        } catch (Exception e){
            throw new ResourceNotFoundException("Transaction not saved");
        }
    }


}
