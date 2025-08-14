package com.washintontech.app.validator;

import com.washintontech.app.annotation.AllowedTransaction;
import com.washintontech.app.domain.TransactionState;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AllowedTransactionType implements ConstraintValidator<AllowedTransaction, TransactionState> {
    private Set<TransactionState> allowedTransactionState;

    @Override
    public void initialize(final AllowedTransaction constraintAnnotation) {
        allowedTransactionState = new HashSet<>(Arrays.asList(constraintAnnotation.value()));
    }

    @Override
    public boolean isValid(final TransactionState value, final ConstraintValidatorContext context) {
        return value == null || allowedTransactionState.contains(value);
    }
}
