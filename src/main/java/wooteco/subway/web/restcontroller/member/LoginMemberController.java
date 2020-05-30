package wooteco.subway.web.restcontroller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.restcontroller.member.methodargumentresolver.LoginMemberId;

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
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMemberId Long id) {
        MemberResponse memberResponse = memberService.findMemberById(id);
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMe(@LoginMemberId Long id,
        @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@LoginMemberId Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
