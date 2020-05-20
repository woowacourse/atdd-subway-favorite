package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.TokenResponse;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@LoginMember @RequestParam Map<String, String> paramMap, HttpSession session) {
        String email = paramMap.get("email");
        String password = paramMap.get("password");
        if (!memberService.loginWithForm(email, password)) {
            throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
        }

        session.setAttribute("loginMemberEmail", email);

        return ResponseEntity.ok().build();
    }

}
