package wooteco.subway.web.member;

import java.net.URI;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.interceptor.Auth;
import wooteco.subway.web.member.interceptor.IsAuth;

@RestController
@RequestMapping(value = "/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @IsAuth
    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest memberRequest) {
        Member member = memberService.createMember(memberRequest);
        return ResponseEntity
            .created(URI.create("/members/" + member.getId()))
            .build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @GetMapping
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @IsAuth(isAuth = Auth.AUTH)
    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(@LoginMember Member member,
        @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(member, param);
        return ResponseEntity.ok().build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @DeleteMapping
    public ResponseEntity<MemberResponse> deleteMember(@LoginMember Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.noContent()
            .build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteRequest favoriteRequest,
        @LoginMember Member member) {
        memberService.addFavorite(member, favoriteRequest);
        return ResponseEntity.ok().build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @GetMapping("/favorites")
    public ResponseEntity<Set<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        Set<FavoriteResponse> favorites = memberService.findFavorites(member);
        return ResponseEntity.ok().body(favorites);
    }

    @IsAuth(isAuth = Auth.AUTH)
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorites(@LoginMember Member member, @PathVariable Long id) {
        memberService.deleteFavorites(id, member);
        return ResponseEntity.noContent().build();
    }
}
