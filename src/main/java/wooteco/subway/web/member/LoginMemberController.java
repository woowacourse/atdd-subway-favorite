package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.dto.DefaultResponse;
import wooteco.subway.web.member.auth.LoginMember;

@RestController
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        String token = memberService.createToken(loginRequest);
        return ResponseEntity.ok().body(DefaultResponse.of(new TokenResponse(token, "bearer")));
    }

    @GetMapping("/me")
    public ResponseEntity<DefaultResponse<MemberResponse>> getMemberOfMineBasic(@LoginMember LoginEmail loginEmail) {
        Member member = memberService.findMemberByEmail(loginEmail);
        return ResponseEntity.ok().body(DefaultResponse.of(MemberResponse.of(member)));
    }
}
