package com.financeflow.financeflow_backend.controller;

import com.financeflow.financeflow_backend.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.financeflow.financeflow_backend.service.TransactionService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private TransactionService transactionService;

    @PostMapping("/create/{id}")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO, @PathVariable("id") Long accountId) {
        TransactionDTO savedTransaction = transactionService.createTransaction(transactionDTO, accountId);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/create/{from_id}/{to_id}")
    public ResponseEntity<String> createTransferTransaction(@RequestBody TransactionDTO transactionDTO,
                                                            @PathVariable("from_id") Long fromAccountId,
                                                            @PathVariable("to_id") Long toAccountId) {
        String transferResult = transactionService.createTransferTransaction(transactionDTO, fromAccountId, toAccountId);
        return new ResponseEntity<>(transferResult, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable("id") Long id) {
        TransactionDTO transaction = transactionService.findTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<List<TransactionDTO>> findTransactionsByAccountId(@PathVariable("id") Long accountId) {
        List<TransactionDTO> transactions = transactionService.findTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionsByUserId(@PathVariable("id") Long userId) {
        List<TransactionDTO> transactions = transactionService.findAllTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionDTO>> findAllTransactions() {
        List<TransactionDTO> transactions = transactionService.findAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable("id") Long id, @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updatedTransaction = transactionService.updateTransaction(transactionDTO, id);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> revertTransaction(@PathVariable("id") Long id) {
        String result = transactionService.revertTransaction(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
