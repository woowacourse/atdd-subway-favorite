package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.*;

import javax.validation.Valid;

@RestController
public class LoginMemberController {
    private final MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping("/me/bearer")
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember MemberResponse memberResponse) {
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/me/bearer")
    public ResponseEntity<Void> updateMember(@LoginMember MemberResponse memberResponse,
                                             @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(memberResponse.getId(), updateMemberRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/bearer")
    public ResponseEntity<Void> deleteMember(@LoginMember MemberResponse memberResponse) {
        memberService.deleteMember(memberResponse.getId());
        return ResponseEntity.ok().build();
    }
}
