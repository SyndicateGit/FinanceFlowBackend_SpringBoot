package com.financeflow.financeflow_backend.controller;

import com.financeflow.financeflow_backend.dto.UserDTO;
import com.financeflow.financeflow_backend.request_response.AuthenticationRequest;
import com.financeflow.financeflow_backend.request_response.AuthenticationResponse;
import com.financeflow.financeflow_backend.service.AuthenticationService;
import com.financeflow.financeflow_backend.request_response.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/fetchUser")
    public ResponseEntity<UserDTO> getUser() {
        return ResponseEntity.ok(authenticationService.getUser());
    }
}
