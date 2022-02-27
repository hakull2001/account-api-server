package com.example.demo.validators;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsInValidator implements ConstraintValidator<IsIn, String> {

    private String[] value;

    @Override
    public void initialize(IsIn constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String fieldValue, ConstraintValidatorContext context) {
        if (fieldValue != null) {
            return Arrays.asList(this.value).contains(fieldValue);
        }
        return true;
    }
}