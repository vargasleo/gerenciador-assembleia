package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {

    private final List<User> userRepository = new ArrayList<>();

    public User findById(String userId) {
        for (User u : userRepository) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    public User save(User user) {
        userRepository.add(user);
        return user;
    }

}
