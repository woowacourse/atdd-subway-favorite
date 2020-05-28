package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.*;
import wooteco.subway.web.member.interceptor.NoValidate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @NoValidate
    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest view) {
        Member member = memberService.createMember(view.toMember());
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @RequestBody UpdateMemberRequest param,
            @LoginMember Member member
    ) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMember(
            @PathVariable Long id,
            @LoginMember Member member
    ) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @NoValidate
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param, HttpSession session) {
        Member member = memberService.loginWithForm(param.getEmail(), param.getPassword());

        String token = memberService.createToken(param);
        session.setAttribute("loginMemberEmail", param.getEmail());

        return ResponseEntity.ok()
                .location(URI.create("/members/" + member.getId()))
                .body(new TokenResponse(token, "bearer"));
    }
}
