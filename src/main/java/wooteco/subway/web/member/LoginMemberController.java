package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginMemberController {
    private final MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "Bearer"));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/bearer")
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> update(
            @LoginMember Member member,
            @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(member.getId(), request);
        return ResponseEntity.ok().build();
    }
}
