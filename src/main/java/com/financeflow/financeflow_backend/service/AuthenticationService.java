package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.request_response.AuthenticationRequest;
import com.financeflow.financeflow_backend.request_response.AuthenticationResponse;
import com.financeflow.financeflow_backend.security.JwtService;
import com.financeflow.financeflow_backend.request_response.RegisterRequest;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.Role;
import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhoneNumber())
                .role(Role.USER)
                .accounts(new ArrayList<Account>())
                .build();

        User initiatedUser = userService.initiateAccounts(user);
        userRepository.save(initiatedUser);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
