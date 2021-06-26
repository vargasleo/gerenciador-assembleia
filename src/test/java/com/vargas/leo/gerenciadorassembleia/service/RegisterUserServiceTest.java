package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateUserRequest;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterUserServiceTest {

    @InjectMocks
    private RegisterUserService registerUserService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldRegisterUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("mockName");
        User response = User.builder().build();

        when(userRepository.save(any(User.class))).thenReturn(response);

        registerUserService.create(createUserRequest);

        verify(userRepository).save(any(User.class));
    }

}
