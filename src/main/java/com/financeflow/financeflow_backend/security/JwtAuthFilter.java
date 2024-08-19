package com.financeflow.financeflow_backend.security;

import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.equals("Bearer null")) {
            filterChain.doFilter(request, response);
            return;
        } else {
            jwt = authHeader.substring(7);
            // Handle expired token with 401 before extracting user email (throws expired exception if expired).
            try {
                jwtService.isTokenExpired(jwt);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired.");
                return;
            }
            userEmail = jwtService.extractUserEmail(jwt);
            // Check if user exist and is not already authenticated.
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = this.userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found."));
                // Update security context holder so context remembers user being already authenticated (has token)
                if (jwtService.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Pass down the filter chain
            filterChain.doFilter(request, response);
        }
    }
}
