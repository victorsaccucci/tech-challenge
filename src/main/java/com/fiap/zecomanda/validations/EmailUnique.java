package com.fiap.zecomanda.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {

    String message() default "E-mail already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
