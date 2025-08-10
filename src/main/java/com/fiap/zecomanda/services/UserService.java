package com.fiap.zecomanda.services;

import com.fiap.zecomanda.commons.config.swagger.openapi.dto.AddressDtoApi;
import com.fiap.zecomanda.commons.config.swagger.openapi.dto.UserDtoApi;
import com.fiap.zecomanda.commons.consts.UserType;
import com.fiap.zecomanda.commons.exceptions.AccessDeniedException;
import com.fiap.zecomanda.commons.exceptions.ResourceAlreadyExistsException;
import com.fiap.zecomanda.commons.exceptions.ResourceNotFoundException;
import com.fiap.zecomanda.commons.security.TokenService;
import com.fiap.zecomanda.dtos.DeleteUserDTO;
import com.fiap.zecomanda.dtos.UpdateUserDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.fiap.zecomanda.commons.consts.UserType.MANAGER;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public void updateUser(UpdateUserDTO dto) {

        // e-mail já usado por outro usuário
        userRepository.findByEmail(dto.email())
                .filter(u -> !u.getId().equals(dto.id()))
                .ifPresent(u -> {
                    throw new ResourceAlreadyExistsException("Email already exists.");
                });

        // login já usado por outro usuário
        userRepository.findByLogin(dto.login())
                .filter(u -> !u.getId().equals(dto.id()))
                .ifPresent(u -> {
                    throw new ResourceAlreadyExistsException("Login already exists.");
                });

        int updated = userRepository.updateUser(dto.name(), dto.email(), dto.phoneNumber(), dto.login(), dto.id());
        if (updated == 0) throw new ResourceNotFoundException("User not found");
    }


    @Transactional
    public DeleteUserDTO delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Regra: não deletar MANAGER
        if (user.getUserType() == UserType.MANAGER) {
            throw new AccessDeniedException("Managers cannot be deleted."); // 403
        }

        int rows = userRepository.hardDeleteById(id);
        if (rows == 0) {
            throw new ResourceNotFoundException("User not found");
        }

        return new DeleteUserDTO(user.getId(), user.getName(), "deleted");
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

        if (user.isEmpty() || MANAGER != user.get().getUserType()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Only managers can access this route.");
        }

        return true;
    }
}
