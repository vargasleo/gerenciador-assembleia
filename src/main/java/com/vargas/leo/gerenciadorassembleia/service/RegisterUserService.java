package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateUserRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateUserResponse;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        User user = this.create(createUserRequest);

        return CreateUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .cpf(user.getCpf())
                .build();
    }

    protected User create(CreateUserRequest createUserRequest) {
        userValidator.validateUserRegister(createUserRequest.getName(), createUserRequest.getCpf());

        User user = User.builder()
                .name(createUserRequest.getName())
                .cpf(createUserRequest.getCpf())
                .build();

        userRepository.save(user);

        return user;
    }

}
