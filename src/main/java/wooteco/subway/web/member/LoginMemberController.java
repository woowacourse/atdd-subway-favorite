package wooteco.subway.web.member;

import javax.validation.Valid;

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
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse jwtToken = memberService.createJwtToken(request);
        return ResponseEntity.ok().body(jwtToken);
    }

    @GetMapping("/me/bearer")
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member request) {
        return ResponseEntity.ok().body(MemberResponse.of(request));
    }
}
