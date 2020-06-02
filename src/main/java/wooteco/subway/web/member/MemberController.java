package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.argumentresolver.annotation.LoginMember;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest view) {
        Member member = memberService.createMember(view.toMember());
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .header("Location", "/members/" + member.getId())
                .build();
    }

    @GetMapping("/auth/members")
    public ResponseEntity<MemberResponse> getMember(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/auth/members")
    public ResponseEntity<MemberResponse> updateMember(@LoginMember Member member, @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(member, param);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/auth/members")
    public ResponseEntity<MemberResponse> deleteMember(@LoginMember Member member) {
        memberService.deleteMember(member.getId());

        return ResponseEntity.noContent().build();
    }
}
