package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.member.dto.*;
import wooteco.subway.web.auth.RequiredAuth;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest view) {
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

    @RequiredAuth
    @PutMapping("/members")
    public ResponseEntity<Void> updateMember(@LoginMember Member member, @RequestBody @Valid UpdateMemberRequest param) {
        memberService.updateMember(member.getId(), param);
        return ResponseEntity.ok().build();
    }

    @RequiredAuth
    @DeleteMapping("/members")
    public ResponseEntity<Void> deleteMember(@LoginMember Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }

    @RequiredAuth
    @PostMapping("/members/favorite")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody FavoriteCreateRequest request) {
        memberService.addFavorite(member, request);
        return ResponseEntity.ok().build();
    }

    @RequiredAuth
    @GetMapping("/members/favorite")
    public ResponseEntity<MemberFavoriteResponse> getFavorite(@LoginMember Member member) {
        Set<FavoriteResponse> favoriteResponses = memberService.findFavorites(member);
        MemberFavoriteResponse memberFavoriteResponse = MemberFavoriteResponse.of(member.getId(), favoriteResponses);
        return ResponseEntity.ok().body(memberFavoriteResponse);
    }

    @RequiredAuth
    @DeleteMapping("/members/favorite/{id}")
    public ResponseEntity<Void> deleteFavoriteById(@LoginMember Member member, @PathVariable Long id) {
        memberService.deleteFavoriteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
