package com.financeflow.financeflow_backend.controller;

import com.financeflow.financeflow_backend.dto.BankDTO;
import com.financeflow.financeflow_backend.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/banks")
public class BankController {
    private final BankService bankService;


    @GetMapping("/{id}")
    public BankDTO getBankById(@PathVariable Long id) {
        return bankService.getBankById(id);
    }

    @GetMapping("/user/{userId}")
    public List<BankDTO> getBanksByUserId(@PathVariable Long userId) {
        return bankService.getBanksByUserId(userId);
    }

    @GetMapping("/all")
    public List<BankDTO> getAllBanks() {
        return bankService.getAllBanks();
    }

    @PostMapping("/create/{userId}")
    public BankDTO createBank(@RequestBody BankDTO bankDTO, @PathVariable Long userId) {
        return bankService.createBank(bankDTO, userId);
    }

    @PutMapping("/{id}")
    public BankDTO updateBank(@PathVariable Long id, @RequestBody BankDTO bankDTO) {
        return bankService.updateBank(id, bankDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
    }

    @GetMapping("/userBanks")
    public List<BankDTO> getUserBanks() {
        return bankService.getUserBanks();
    }
}
