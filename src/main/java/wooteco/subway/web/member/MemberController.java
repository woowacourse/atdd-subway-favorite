package wooteco.subway.web.member;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Set;

import javax.validation.Valid;

import org.apache.tomcat.util.digester.DocumentProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
@RequestMapping(value = "/members", produces = "application/json;charset=UTF-8")
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
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@RequestAttribute String email, @PathVariable Long id,
                                                       @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(email, id, param);
        return ResponseEntity.ok().build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> deleteFavorites(@PathVariable Long id, @LoginMember Member member) {
        // memberService.deleteFavorites(id, member);
        return ResponseEntity.noContent()
            .build();
    }
}
