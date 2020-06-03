package wooteco.subway.web.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping({"/me"})
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMember(@LoginMember Member member,
        @Valid @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(member, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@LoginMember Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.noContent().build();
    }
}
