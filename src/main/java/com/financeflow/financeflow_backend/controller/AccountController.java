package com.financeflow.financeflow_backend.controller;

import com.financeflow.financeflow_backend.dto.AccountDTO;
import com.financeflow.financeflow_backend.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    @PostMapping("/create/{bankId}")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO, @PathVariable("bankId") Long bankId) {
        AccountDTO savedAccount = accountService.createAccount(accountDTO, bankId);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("id") Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable("id") Long id) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(id);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable("id") Long id , @RequestBody AccountDTO accountDTO) {
        AccountDTO savedAccount = accountService.updateAccount(id ,accountDTO);
        return ResponseEntity.ok(savedAccount);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully.");
    }

    @PutMapping("/deposit/{id}")
    public ResponseEntity<BigDecimal> deposit(@PathVariable("id") Long id, @RequestBody BigDecimal amount) {
        BigDecimal newBalance = accountService.deposit(id, amount);
        return ResponseEntity.ok(newBalance);
    }

    @PutMapping("/withdrawal/{id}")
    public ResponseEntity<BigDecimal> withdrawal(@PathVariable("id") Long id, @RequestBody BigDecimal amount) {
        BigDecimal newBalance = accountService.withdrawal(id, amount);
        return ResponseEntity.ok(newBalance);
    }

    @PutMapping("/transfer/{from_id}/{to_id}")
    public ResponseEntity<String> transfer(@PathVariable("from_id") Long from_id,
                                           @PathVariable("to_id") Long to_id,
                                           @RequestBody BigDecimal amount) {
        accountService.transfer(from_id, to_id, amount);
        return ResponseEntity.ok("Amount transferred successfully.");
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getUserAccounts() {
        List<AccountDTO> accounts = accountService.getUserAcounts();
        return ResponseEntity.ok(accounts);
    }
}
