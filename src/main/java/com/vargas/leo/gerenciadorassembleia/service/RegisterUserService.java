package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateUserRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateUserResponse;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Transactional
    protected User create(CreateUserRequest createUserRequest) {
        try {
            userValidator.validateUserRegister(createUserRequest.getName(), createUserRequest.getCpf());
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw e;
        }

        User user = User.builder()
                .name(createUserRequest.getName())
                .cpf(createUserRequest.getCpf())
                .build();

        userRepository.save(user);

        return user;
    }

}
