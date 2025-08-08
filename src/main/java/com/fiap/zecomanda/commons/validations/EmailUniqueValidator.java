package com.fiap.zecomanda.commons.validations;

import com.fiap.zecomanda.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    private final UserRepository userRepository;

    public EmailUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) return true; // @NotBlank/@Email cuidam disso
        return userRepository.findByEmail(email).isEmpty();
    }
}
