package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.member.dto.*;
import wooteco.subway.web.auth.Auth;
import wooteco.subway.web.auth.RequiredAuth;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<URI> createMember(@RequestBody @Valid MemberRequest request) {
        Member member = memberService.createMember(request.toMember());
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .build();
    }

    @RequiredAuth(type = Auth.AUTH_BY_EMAIL)
    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberByEmail(@RequestParam String email) {
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @RequiredAuth(type = Auth.AUTH_BY_ID)
    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(id, request);
        return ResponseEntity.ok().build();
    }

    @RequiredAuth(type = Auth.AUTH_BY_ID)
    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @RequiredAuth(type = Auth.AUTH_BY_ID)
    @PostMapping("/members/{id}/favorite")
    public ResponseEntity<URI> createFavorite(@PathVariable Long id, @RequestBody FavoriteCreateRequest request) {
        memberService.addFavorite(id, request);
        return ResponseEntity.created(URI.create("/members/" + id + "/favorite"))
                .build();
    }

    @RequiredAuth(type = Auth.AUTH_BY_ID)
    @GetMapping("/members/{id}/favorite")
    public ResponseEntity<MemberFavoriteResponse> getFavorite(@PathVariable Long id) {
        MemberFavoriteResponse memberFavoriteResponse = memberService.findFavorites(id);
        return ResponseEntity.ok().body(memberFavoriteResponse);
    }

    @RequiredAuth(type = Auth.AUTH_BY_ID)
    @DeleteMapping("/members/{memberId}/favorite/{favoriteId}")
    public ResponseEntity<MemberFavoriteResponse> deleteFavoriteById(@PathVariable Long memberId, @PathVariable Long favoriteId) {
        memberService.deleteFavoriteById(memberId,favoriteId);
        return ResponseEntity.noContent().build();
    }
}
