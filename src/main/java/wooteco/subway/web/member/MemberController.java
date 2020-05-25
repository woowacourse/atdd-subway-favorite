package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.aspect.NoValidate;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @NoValidate
    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest view) {
        Member member = memberService.createMember(view.toMember());
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .build();
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberByEmail(
            @RequestParam String email,
            @RequestAttribute("loginMemberEmailSession") String sessionEmail,
            @RequestAttribute("loginMemberEmailJwt") String tokenEmail
    ) {
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @RequestBody UpdateMemberRequest param,
            @RequestAttribute("loginMemberEmailSession") String sessionEmail,
            @RequestAttribute("loginMemberEmailJwt") String tokenEmail) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(
            @PathVariable Long id,
            @RequestAttribute("loginMemberEmailSession") String sessionEmail,
            @RequestAttribute("loginMemberEmailJwt") String tokenEmail) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
