package wooteco.subway.web.prehandler.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import wooteco.subway.web.exception.NotMatchPasswordException;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
   private String field;
   private String fieldMatch;


   public void initialize(PasswordMatch constraint) {
      this.field = constraint.field();
      this.fieldMatch = constraint.fieldMatch();
   }

   public boolean isValid(Object value, ConstraintValidatorContext context) {
      Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
      Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

      if (!Objects.equals(fieldValue, fieldMatchValue)) {
         throw new NotMatchPasswordException("패스워드 확인이 올바르지 않습니다.");
      }
      return true;
   }
}
