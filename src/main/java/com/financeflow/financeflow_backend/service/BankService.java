package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.dto.BankDTO;


import java.math.BigDecimal;
import java.util.List;

public interface BankService {
    BankDTO getBankById(Long id);
    List<BankDTO> getBanksByUserId(Long userId);
    List<BankDTO> getAllBanks();

    BankDTO createBank(BankDTO bankDTO, Long userId);
    BankDTO updateBank(Long id, BankDTO accountDTO);
    void deleteBank(Long id);

    List<BankDTO> getUserBanks();
}
