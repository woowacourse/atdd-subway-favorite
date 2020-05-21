package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.dto.DefaultResponse;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<DefaultResponse<Void>> createMember(@RequestBody MemberRequest memberRequest) {
        Long memberId = memberService.createMember(memberRequest);
        return ResponseEntity
                .created(URI.create("/members/" + memberId))
                .body(DefaultResponse.empty());
    }
//
//    @GetMapping("/members")
//    public ResponseEntity<DefaultResponse<MemberResponse>> getMemberByEmail(@RequestParam String email, @LoginMember LoginEmail loginEmail) {
//        Member member = memberService.findMemberByEmail(email, loginEmail);
//        return ResponseEntity.ok().body(DefaultResponse.of(MemberResponse.of(member)));
//    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
