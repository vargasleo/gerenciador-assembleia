package com.vargas.leo.gerenciadorassembleia.controller;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateUserRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateUserResponse;
import com.vargas.leo.gerenciadorassembleia.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserService registerUserService;

    @PostMapping("create/user")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        return registerUserService.createUser(createUserRequest);
    }

}
