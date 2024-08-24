package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.BankDTO;
import com.financeflow.financeflow_backend.entity.Bank;
import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.BankMapper;
import com.financeflow.financeflow_backend.repository.BankRepository;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    @Override
    public BankDTO getBankById(Long id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found"));
        return BankMapper.mapToBankDTO(bank);
    }

    @Override
    public List<BankDTO> getBanksByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Bank> banks = user.getBanks();
        return banks.stream()
                .map(BankMapper::mapToBankDTO)
                .toList();
    }

    @Override
    public List<BankDTO> getAllBanks() {
        List<Bank> banks = bankRepository.findAll();
        return banks.stream()
                .map(BankMapper::mapToBankDTO)
                .toList();
    }

    @Override
    public BankDTO createBank(BankDTO bankDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Bank bank = BankMapper.mapToBank(bankDTO);
        Bank savedBank = bankRepository.save(bank);
        User updatedUser = user.addBank(savedBank);
        userRepository.save(updatedUser);
        return BankMapper.mapToBankDTO(savedBank);
    }

    @Override
    public BankDTO updateBank(Long id, BankDTO accountDTO) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found"));
        bank.setName(accountDTO.getName());
        Bank updatedBank = bankRepository.save(bank);
        return BankMapper.mapToBankDTO(updatedBank);
    }

    @Override
    public void deleteBank(Long id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found"));
        User user = userRepository.findByBanksContaining(bank)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.removeBank(bank);
        userRepository.save(user);
    }

    @Override
    public List<BankDTO> getUserBanks() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Bank> banks = user.getBanks();
        return banks.stream()
                .map(BankMapper::mapToBankDTO)
                .toList();
    }
}
