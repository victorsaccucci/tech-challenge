package com.fiap.zecomanda.commons.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginUniqueValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUnique {

    String message() default "Login invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
