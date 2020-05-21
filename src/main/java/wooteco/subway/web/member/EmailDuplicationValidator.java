package wooteco.subway.web.member;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import wooteco.subway.exception.NoSuchMemberException;
import wooteco.subway.service.member.MemberService;

@Component
public class EmailDuplicationValidator implements ConstraintValidator<EmailMatcher, String> {
    private final MemberService memberService;

    public EmailDuplicationValidator(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void initialize(EmailMatcher constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            memberService.findMemberByEmail(value);
            return false;
        } catch (NoSuchMemberException exception) {
            return true;
        }
    }
}
