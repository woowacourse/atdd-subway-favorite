package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
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
    public ResponseEntity<DefaultResponse<MemberResponse>> getMyInfo(@LoginMember LoginEmail loginEmail) {
        Member member = memberService.findMemberByEmail(loginEmail);
        return ResponseEntity.ok().body(DefaultResponse.of(MemberResponse.of(member)));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyInfo(@LoginMember LoginEmail loginEmail) {
        memberService.deleteByEmail(loginEmail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMyInfo(@RequestBody UpdateMemberRequest updateMemberRequest, @LoginMember LoginEmail loginEmail) {
        memberService.updateMember(updateMemberRequest, loginEmail);
        return ResponseEntity.noContent().build();
    }
}
