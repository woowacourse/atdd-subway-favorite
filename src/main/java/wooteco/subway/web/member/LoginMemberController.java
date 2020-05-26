package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping("/me/bearer")
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMember(@LoginMember Member member, @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(member, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@LoginMember Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @Valid @RequestBody FavoriteCreateRequest param) {
        memberService.addFavorite(member, param);
        return ResponseEntity.created(URI.create("/me/favorites")).build();
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@LoginMember Member member) {
        List<FavoriteResponse> allFavorites = memberService.findAllFavorites(member);
        return ResponseEntity.ok().body(allFavorites);
    }

    @DeleteMapping("/me/favorites/{id}")
    public ResponseEntity<Void> removeFavorite(@LoginMember Member member, @PathVariable Long id) {
        memberService.removeFavorite(member, id);
        return ResponseEntity.noContent().build();
    }

}
