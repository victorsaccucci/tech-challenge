package com.fiap.zecomanda.commons.validations;

import com.fiap.zecomanda.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginUniqueValidator implements ConstraintValidator<LoginUnique, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null) {
            return true;
        }
        return userRepository.findByLogin(login).isEmpty();
    }
}
