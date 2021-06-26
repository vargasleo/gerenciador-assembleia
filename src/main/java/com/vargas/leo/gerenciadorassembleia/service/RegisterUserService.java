package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateUserRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateUserResponse;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;

    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        User user = this.create(createUserRequest);

        return CreateUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    protected User create(CreateUserRequest createUserRequest) {
        User user =  User.builder()
                .name(createUserRequest.getName())
                .build();

        userRepository.save(user);

        return user;
    }

}
