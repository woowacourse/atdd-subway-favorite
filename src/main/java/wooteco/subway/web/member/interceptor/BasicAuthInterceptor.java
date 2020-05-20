//package wooteco.subway.web.member.interceptor;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import wooteco.subway.domain.member.Member;
//import wooteco.subway.service.member.MemberService;
//import wooteco.subway.web.member.AuthorizationExtractor;
//import wooteco.subway.web.member.InvalidAuthenticationException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class BasicAuthInterceptor implements HandlerInterceptor {
//    private AuthorizationExtractor authExtractor;
//    private MemberService memberService;
//
//    public BasicAuthInterceptor(AuthorizationExtractor authExtractor, MemberService memberService) {
//        this.authExtractor = authExtractor;
//        this.memberService = memberService;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // TODO: Authorization 헤더를 통해 Basic 값을 추출 (authExtractor.extract() 메서드 활용)
//
//        // TODO: 추출한 Basic 값을 Base64를 통해 email과 password 값 추출(Base64.getDecoder().decode() 메서드 활용)
//
//        String email = "";
//        String password = "";
//
//        Member member = memberService.findMemberByEmail(email);
//        if (!member.checkPassword(password)) {
//            throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
//        }
//
//        request.setAttribute("loginMemberEmail", email);
//        return true;
//    }
//}
