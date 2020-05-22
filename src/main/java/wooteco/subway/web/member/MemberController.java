package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.web.dto.DefaultResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<DefaultResponse<Void>> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        Long memberId = memberService.createMember(memberRequest);
        return ResponseEntity
                .created(URI.create("/members/" + memberId))
                .body(DefaultResponse.empty());
    }

}
