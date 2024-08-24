package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.AccountDTO;
import com.financeflow.financeflow_backend.dto.BankDTO;
import com.financeflow.financeflow_backend.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    @Override
    public BankDTO getBankById(Long id) {
        return null;
    }

    @Override
    public List<BankDTO> getBanksByUserId(Long userId) {
        return null;
    }

    @Override
    public List<BankDTO> getAllBanks() {
        return null;
    }

    @Override
    public BankDTO createBank(BankDTO bankDTO, Long userId) {
        return null;
    }

    @Override
    public BankDTO updateBank(Long id, BankDTO accountDTO) {
        return null;
    }

    @Override
    public void deleteBank(Long id) {

    }

    @Override
    public List<BankDTO> getUserBanks() {
        return null;
    }
}
