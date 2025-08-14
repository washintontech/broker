package com.washintontech.app.annotation;

import com.washintontech.app.domain.TransactionState;
import com.washintontech.app.validator.AllowedTransactionType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedTransactionType.class)
public @interface AllowedTransaction {
    TransactionState[] value();

    String message() default "Invalid Transaction Type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
