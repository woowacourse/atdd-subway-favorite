package wooteco.subway.web.member.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import wooteco.subway.service.member.MemberService;

@Component
public class EmailMatchValidator implements ConstraintValidator<DuplicateCheck, String> {
    private MemberService memberService;

    public EmailMatchValidator(MemberService memberService) {
        this.memberService = memberService;
    }

    public void initialize(DuplicateCheck constraint) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        return memberService.isNotExistEmail(email);
    }
}
