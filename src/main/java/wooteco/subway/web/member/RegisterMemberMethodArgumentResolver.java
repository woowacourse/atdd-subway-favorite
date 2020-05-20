package wooteco.subway.web.member;

import static org.springframework.web.context.request.RequestAttributes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRawRequest;
import wooteco.subway.service.member.dto.MemberRequest;

@Component
public class RegisterMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public RegisterMemberMethodArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RegisterMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        BufferedReader reader = request.getReader();
        ObjectMapper objectMapper = new ObjectMapper();
        MemberRawRequest memberRawRequest = objectMapper.readValue(reader, MemberRawRequest.class);

        String email = memberRawRequest.getEmail();
        String password = memberRawRequest.getPassword();
        String passwordCheck = memberRawRequest.getPasswordCheck();

        if (!Objects.equals(password, passwordCheck)) {
            throw new NotMatchPasswordException("패스워드가 일치하지 않습니다.");
        }
        if (memberService.isExistEmail(email)) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
        return memberRawRequest.toMemberRequest();
    }
}
