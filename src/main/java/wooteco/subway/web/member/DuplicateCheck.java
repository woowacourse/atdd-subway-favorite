package wooteco.subway.web.member;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EmailMatchValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateCheck {
    String message() default "이메일이 중복됩니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
