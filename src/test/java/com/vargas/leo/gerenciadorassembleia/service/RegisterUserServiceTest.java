package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateUserRequest;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterUserServiceTest {

    @InjectMocks
    private RegisterUserService registerUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Test
    public void shouldRegisterUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("mockName");
        User response = User.builder().build();

        when(userRepository.save(any(User.class))).thenReturn(response);

        User result = registerUserService.create(createUserRequest);

        assertEquals(createUserRequest.getName(), result.getName());
        assertEquals(createUserRequest.getCpf(), result.getCpf());

        verify(userValidator).validateUserRegister(createUserRequest.getName(), createUserRequest.getCpf());
        verify(userRepository).save(any(User.class));
    }

}
