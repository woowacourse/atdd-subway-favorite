package wooteco.subway.web.member;

import static wooteco.subway.web.member.MemberController.MEMBER_URI;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;

@RequestMapping(MEMBER_URI)
@RestController
public class MemberController {
    public static final String MEMBER_URI = "/members";

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity
            .created(URI.create(MEMBER_URI + "/" + response.getId()))
            .build();
    }
}
