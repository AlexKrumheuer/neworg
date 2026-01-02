package com.neworg.neworg.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(UserRegisterDTO user) {
        if(userRepository.findByEmail(user.email()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        String encryptedPassword = passwordEncoder.encode(user.password());
        User newUser = new User(user.username(), user.email(), encryptedPassword);
        return userRepository.save(newUser);
    }

    public User logarUser(UserLoginDTO user) {
        User existingUser = userRepository.findByEmail(user.email());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not exists");
        }
        boolean passwordMatches = passwordEncoder.matches(user.password(), existingUser.getPassword());
        if (!passwordMatches) {
            throw new IllegalArgumentException("Credenciais Inválidas");
        }
        return existingUser;
    }
}
