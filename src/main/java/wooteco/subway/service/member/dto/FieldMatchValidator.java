package wooteco.subway.service.member.dto;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String other;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        field = constraintAnnotation.field();
        other = constraintAnnotation.other();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object otherValue = new BeanWrapperImpl(value).getPropertyValue(other);

        return Objects.equals(fieldValue, otherValue);
    }
}