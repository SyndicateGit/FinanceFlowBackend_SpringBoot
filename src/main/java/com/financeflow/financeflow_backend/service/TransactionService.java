package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long accountId);
    String createTransferTransaction(TransactionDTO transactionDTO, Long from_account_id, Long to_account_id);

    TransactionDTO findTransactionById(Long id);
    List<TransactionDTO> findTransactionsByAccountId(Long accountId);

    List<TransactionDTO> findAllTransactionsByUserId(Long userId);

    List<TransactionDTO> findAllTransactions();

    String revertTransaction(Long id);
}
