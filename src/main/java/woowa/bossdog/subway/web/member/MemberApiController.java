package woowa.bossdog.subway.web.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woowa.bossdog.subway.service.Member.MemberService;
import woowa.bossdog.subway.service.Member.dto.MemberRequest;
import woowa.bossdog.subway.service.Member.dto.MemberResponse;
import woowa.bossdog.subway.service.Member.dto.UpdateMemberRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MemberRequest request) {
        final MemberResponse response = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + response.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> list() {
        final List<MemberResponse> responses = memberService.listMembers();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> find(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(memberService.findMember(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @RequestBody UpdateMemberRequest request
    ) {
        memberService.updateMember(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
