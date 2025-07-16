package com.fiap.zecomanda.services;

import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.ChangePasswordDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public void updatePassword(Long id, ChangePasswordDTO passwordDTO) {
        User user = userRepository.findById(id).orElseThrow();

        boolean validPassword = passwordEncoder.matches(passwordDTO.currentPassword(), user.getPassword());
        
        if (!validPassword) {
        throw new IllegalArgumentException("The current passoword isn`t incorrect.");
    }
        user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
        userRepository.save(user);
    }

    public void updateUser(User user, Long id) {
        var update = this.userRepository.update(user, id);
        if (update == 0) {
            throw new RuntimeException("User not found");
        }
    }

    public void delete(Long id) {
        var delete = this.userRepository.delete(id);
        if (delete == 0) {
            throw new RuntimeException("User not found");
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User extractUserSubject(String token){
        String subjectLogin = tokenService.extractSubject(token);
        User foundUser = userRepository.findUserByLogin(subjectLogin);
        return foundUser;
    }
}
