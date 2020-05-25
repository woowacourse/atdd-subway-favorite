package wooteco.subway.web.member;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Favorite;
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
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @IsAuth
    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest memberRequest) {
        Member member = memberService.createMember(memberRequest);
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @IsAuth(isAuth = Auth.AUTH)
    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@RequestAttribute String email, @PathVariable Long id,
                                                       @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(email, id, param);
        return ResponseEntity.ok().build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @PostMapping("/members/favorites")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteRequest favoriteRequest,
                                            @LoginMember Member member) {
        memberService.addFavorite(member, favoriteRequest);
        return ResponseEntity.ok().build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @GetMapping("/members/favorites")
    public ResponseEntity<Set<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok().body(memberService.findFavorites(member));
    }

    @IsAuth(isAuth = Auth.AUTH)
    @DeleteMapping("/members/favorites/{id}")
    public ResponseEntity<Void> deleteFavorites(@PathVariable Long id, @LoginMember Member member) {
        return ResponseEntity.noContent().build();
    }
}
