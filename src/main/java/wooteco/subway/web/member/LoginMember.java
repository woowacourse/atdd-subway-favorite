package wooteco.subway.web.member;

import static org.springframework.web.servlet.HandlerMapping.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.web.context.request.NativeWebRequest;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.exceptions.InvalidAuthenticationException;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginMember {
    Type type() default Type.NONE;

    enum Type {
        ID(Type::validateId),
        EMAIL(Type::validateEmail),
        NONE((request, member) -> member);

        private final BiFunction<NativeWebRequest, Member, Member> validator;

        Type(final BiFunction<NativeWebRequest, Member, Member> validator) {
            this.validator = validator;
        }

        public Member validate(final NativeWebRequest webRequest, final Member member) {
            return validator.apply(webRequest, member);
        }

        private static Member validateEmail(final NativeWebRequest webRequest, final Member member) {
            String emailParameter = webRequest.getParameter("email");
            if (!Objects.equals(member.getEmail(), emailParameter)) {
                throw new InvalidAuthenticationException("비정상적인 로그인");
            }
            return member;
        }

        private static Member validateId(final NativeWebRequest webRequest, final Member member) {
            Map<String, String> pathVariables
                = (Map<String, String>)webRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
            Long id = Long.valueOf(pathVariables.get("id"));
            if (!Objects.equals(member.getId(), id)) {
                throw new InvalidAuthenticationException("비정상적인 로그인");
            }
            return member;
        }
    }
}
