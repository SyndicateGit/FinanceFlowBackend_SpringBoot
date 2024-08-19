package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.UserDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.UserMapper;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.financeflow.financeflow_backend.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public User initiateAccounts(User user) {
        Account savingsAccount = new Account();
        savingsAccount.initiateSavingsAccount();
        Account debitAccount = new Account();
        debitAccount.initiateDebitAccount();
        Account creditAccount = new Account();
        creditAccount.initiateCreditAccount();
        user.addAccount(savingsAccount);
        user.addAccount(debitAccount);
        user.addAccount(creditAccount);
        return user;
    }
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (isEmailOrPhoneAlreadyExists(userDTO.getEmail(), userDTO.getPhone())) {
            throw new ResourceNotFoundException("Email or phone number already exists");
        }
        User user = UserMapper.mapToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // All users will have a savings, debit and credit account
        User initiatedUser = initiateAccounts(user);
        User  savedUser = userRepository.save(initiatedUser);

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

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());

        // Accounts left as is.
        // Update user is only for user info

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

    public UserDTO getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserDTO(user);
    }

    private boolean isEmailOrPhoneAlreadyExists(String email, String phone) {
        return userRepository.existsByEmail(email) || userRepository.existsByPhone(phone);
    }
}
