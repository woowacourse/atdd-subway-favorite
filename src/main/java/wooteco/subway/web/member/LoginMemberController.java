package wooteco.subway.web.member;

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

import javax.servlet.http.HttpSession;

@RestController
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param, HttpSession session) {
        // TODO: 2020/05/20 session 생성 주체 확인
        if (!memberService.loginWithForm(param.getEmail(), param.getPassword())) {
            throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
        }

        String token = memberService.createToken(param);
        session.setAttribute("loginMemberEmail", param.getEmail());

        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping({"/mypage"})
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }
}
