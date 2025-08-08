package com.fiap.zecomanda.services;

import com.fiap.zecomanda.commons.config.swagger.openapi.dto.AddressDtoApi;
import com.fiap.zecomanda.commons.config.swagger.openapi.dto.UserDtoApi;
import com.fiap.zecomanda.commons.consts.UserType;
import com.fiap.zecomanda.commons.security.TokenService;
import com.fiap.zecomanda.dtos.UpdateUserDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        var userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        var user = userOptional.get();
        user.setDeleted(true);
        var delete = this.userRepository.save(user);
        System.out.println(""+ delete);
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
                            user.getName(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getUserType(),
                            user.getLogin(),
                            user.getUpdatedAtFormated(),
                            user.getDeleted(),
                            addressDto);
                })
                .toList();
    }

    public Optional<User> extractUserSubject(String tokenHeader) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid or missing token");
        }

        String token = tokenHeader.replace("Bearer ", "");
        String subjectLogin = tokenService.extractSubject(token);
        return userRepository.findById(Long.valueOf(subjectLogin));
    }

    public boolean checkUserRoleAdmin(String authorizationHeader) {
        Optional<User> user = extractUserSubject(authorizationHeader);

        if (user.isEmpty() || UserType.MANAGER != user.get().getUserType()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Only managers can access this route.");
        }

        return true;
    }
}
