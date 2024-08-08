package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long accountId);
}
