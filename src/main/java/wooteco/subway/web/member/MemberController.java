package wooteco.subway.web.member;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest view) {
        Member member = memberService.createMember(view.toMember());
        return ResponseEntity
            .created(URI.create("/members/" + member.getId()))
            .build();
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberByEmail(@RequestParam String email) {
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/members")
    public ResponseEntity<MemberResponse> updateMember(@RequestBody UpdateMemberRequest param, @LoginMember @Valid Member member) {
        if (member.isNotMe(param.getEmail())) {
            throw new IllegalArgumentException(Member.NOT_ME_MESSAGE);
        }
        memberService.updateMember(member, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members")
    public ResponseEntity<Void> deleteMember(@LoginMember @Valid Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }
}
