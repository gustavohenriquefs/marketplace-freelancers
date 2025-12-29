package com.ufc.quixada.api.presentation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;
    private boolean nullable = false;
    private boolean empty = false;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants()).map(Enum::name).toList();
        nullable = annotation.nullable();
        empty = annotation.empty();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        String allowedValuesString = String.join(", ", acceptedValues);
        if (value == null) {
            return validCheck(nullable, context, allowedValuesString);
        }

        if (value.isEmpty()) {
            return validCheck(empty, context, allowedValuesString);
        }

        boolean isValid = acceptedValues.contains(value.toString());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(
                            "Valor inválido. Os valores permitidos são: " + allowedValuesString)
                    .addConstraintViolation();
        }

        return isValid;
    }

    private boolean validCheck(
            boolean check, ConstraintValidatorContext context, String allowedValuesString) {
        if (check) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(
                            "É necessário que o campo possua um dos seguintes valores: " + allowedValuesString)
                    .addConstraintViolation();
            return false;
        }
    }
}
