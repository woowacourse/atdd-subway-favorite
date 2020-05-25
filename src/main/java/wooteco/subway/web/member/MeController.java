package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
@RequestMapping("/me")
public class MeController {
    private final MemberService memberService;

    public MeController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member request) {
        return ResponseEntity.ok().body(MemberResponse.of(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateMember(@LoginMember Member request, @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(request.getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@LoginMember Member request) {
        memberService.deleteMember(request.getId());
        return ResponseEntity.noContent().build();
    }
}
