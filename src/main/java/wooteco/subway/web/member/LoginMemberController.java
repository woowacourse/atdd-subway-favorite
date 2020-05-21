package wooteco.subway.web.member;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

@RestController
public class LoginMemberController {
    private final MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = memberService.createToken(request);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpSession session) {
        String email = request.getEmail();
        String password = request.getPassword();
        if (!memberService.loginWithForm(email, password)) {
            throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
        }
        session.setAttribute("loginMemberEmail", email);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/me/basic", "/me/session", "/me/bearer"})
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }
}
