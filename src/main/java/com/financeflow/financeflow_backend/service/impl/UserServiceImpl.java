package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.UserDTO;
import com.financeflow.financeflow_backend.mapper.UserMapper;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.financeflow.financeflow_backend.entity.User;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.mapToUser(userDTO);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDTO(savedUser);
    }
}
