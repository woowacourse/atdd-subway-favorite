package wooteco.subway.web.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMemberOfMine(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMemberOfMine(@LoginMember Member member, @Valid @RequestBody
        UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(member.getId(), updateMemberRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@LoginMember Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }
}
