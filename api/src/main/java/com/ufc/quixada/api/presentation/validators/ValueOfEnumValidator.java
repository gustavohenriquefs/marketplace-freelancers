package com.ufc.quixada.api.presentation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {
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
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String allowedValuesString = String.join(", ", acceptedValues);

        if (value == null) {
            return validCheck(nullable, context, allowedValuesString);
        }

        String stringValue;
        if (value instanceof Enum<?>) {
            stringValue = ((Enum<?>) value).name();
        } else if (value instanceof CharSequence) {
            stringValue = value.toString();

            if (stringValue.isEmpty()) {
                return validCheck(empty, context, allowedValuesString);
            }
        } else {
            stringValue = value.toString();
        }

        boolean isValid = acceptedValues.contains(stringValue);

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
