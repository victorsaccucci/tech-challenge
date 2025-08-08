package com.fiap.zecomanda.commons.validations;

import com.fiap.zecomanda.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class LoginUniqueValidator implements ConstraintValidator<LoginUnique, String> {

    private UserRepository userRepository;

    public LoginUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null || login.isBlank()) return true;
        return userRepository.findByLogin(login).isEmpty();
    }
}
