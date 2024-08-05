package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.UserDTO;
import com.financeflow.financeflow_backend.entity.User;

public class UserMapper {
    public static UserDTO mapToUserDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getRole()
        );
    }

    public static User mapToUser(UserDTO userDTO){
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getPhone(),
                userDTO.getRole()
        );
    }
}
