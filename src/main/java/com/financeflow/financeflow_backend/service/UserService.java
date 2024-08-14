package com.financeflow.financeflow_backend.service;

import com.financeflow.financeflow_backend.dto.UserDTO;

import java.util.List;


public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

}
