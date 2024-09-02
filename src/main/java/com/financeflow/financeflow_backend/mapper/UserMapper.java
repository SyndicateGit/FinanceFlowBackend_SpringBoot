package com.financeflow.financeflow_backend.mapper;

import com.financeflow.financeflow_backend.dto.UserDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.entity.Bank;
import com.financeflow.financeflow_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class UserMapper {
//    private static UserRepository userRepository;
//
//    @Autowired
//    public UserMapper(UserRepository userRepository) {
//        UserMapper.userRepository = userRepository;
//    }
    public static UserDTO mapToUserDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
