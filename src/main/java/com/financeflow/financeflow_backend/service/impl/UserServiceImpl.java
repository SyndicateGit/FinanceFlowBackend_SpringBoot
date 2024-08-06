package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.UserDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.UserMapper;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.financeflow.financeflow_backend.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    public void initiateAccounts(User user){
        Account savingsAccount = new Account();
        Account debitAccount = new Account();
        Account creditAccount = new Account();

        savingsAccount.initiateSavingsAccount(user);
        debitAccount.initiateDebitAccount(user);
        creditAccount.initiateCreditAccount(user);

        accountRepository.save(savingsAccount);
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
    }
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.mapToUser(userDTO);
        User savedUser = userRepository.save(user);

        // All users will have a savings, debit and credit account
        initiateAccounts(savedUser);

        return UserMapper.mapToUserDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given id: " + id));
       return UserMapper.mapToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userDTOSList = userRepository.findAll();
        return userDTOSList
                .stream()
                .map(UserMapper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given id: " + id)
        );

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());

        userRepository.save(user);

        return UserMapper.mapToUserDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given id: " + id)
                );
        userRepository.deleteById(id);
    }

}
