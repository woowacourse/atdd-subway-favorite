package wooteco.subway.web.member;

import java.net.URI;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest view) {
        MemberResponse member = memberService.createMember(view.toMember());
        return ResponseEntity
            .created(URI.create("/members/" + member.getId()))
            .build();
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberByEmail
        (
            @LoginMember Member member,
            @Param("email") String email
        ) {
        if (!member.isAuthenticated(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(
        @LoginMember Member member,
        @PathVariable Long id,
        @RequestBody UpdateMemberRequest param
    ) {
        if (!member.isAuthenticated(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        memberService.updateMember(member, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@LoginMember Member member, @PathVariable Long id) {
        if (!member.isAuthenticated(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        memberService.deleteMember(member);
        return ResponseEntity.noContent().build();
    }
}
