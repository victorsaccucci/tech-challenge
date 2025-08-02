package com.fiap.zecomanda.services;

import com.fiap.zecomanda.common.config.swagger.openapi.dto.AddressDtoApi;
import com.fiap.zecomanda.common.config.swagger.openapi.dto.UserDtoApi;
import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.UpdateUserDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public void updateUser(UpdateUserDTO user, Long id) {
        var update = this.userRepository.updateUser(user.name(), user.email(), user.phoneNumber(), user.login(), id);
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

    public List<UserDtoApi> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    AddressDtoApi addressDto = null;
                    if (user.getAddress() != null) {
                        addressDto = new AddressDtoApi(
                                user.getAddress().getStreet(),
                                user.getAddress().getNeighborhood(),
                                user.getAddress().getCity(),
                                user.getAddress().getNumber(),
                                user.getAddress().getState(),
                                user.getAddress().getCountry()
                        );
                    }
                    return new UserDtoApi(
                            user.getId(),
                            user.getLogin(),
                            user.getPassword(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null,
                            addressDto);
                })
                .toList();
    }

    public Optional<User> extractUserSubject(String tokenHeader) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token inválido ou ausente");
        }

        String token = tokenHeader.substring(7);
        String subjectLogin = tokenService.extractSubject(token);
        return userRepository.findById(Long.valueOf(subjectLogin));
    }

}
