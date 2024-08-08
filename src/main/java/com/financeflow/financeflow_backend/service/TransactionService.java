package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long accountId);
    String createTransferTransaction(TransactionDTO transactionDTO, Long from_account_id, Long to_account_id);
}
