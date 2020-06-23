package woowa.bossdog.subway.web.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.service.member.MemberService;
import woowa.bossdog.subway.service.member.dto.LoginRequest;
import woowa.bossdog.subway.service.member.dto.MemberResponse;
import woowa.bossdog.subway.service.member.dto.TokenResponse;
import woowa.bossdog.subway.service.member.dto.UpdateMemberRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class LoginMemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = memberService.createToken(request);
        return ResponseEntity.ok().body(new TokenResponse(token, "Bearer"));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getLoginMember(@LoginMember Member member) {
        final MemberResponse memberResponse = MemberResponse.from(member);
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping
    public ResponseEntity<Void> update(
            @LoginMember Member member,
            @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(member.getId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @LoginMember Member member
    ) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }
}
