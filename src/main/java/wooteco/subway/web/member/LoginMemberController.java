package wooteco.subway.web.member;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteExistResponse;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
public class LoginMemberController {
    private MemberService memberService;
    private FavoriteService favoriteService;

    public LoginMemberController(MemberService memberService,
        FavoriteService favoriteService) {
        this.memberService = memberService;
        this.favoriteService = favoriteService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMemberOfMine(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMemberOfMine(@LoginMember Member member, @Valid @RequestBody
        UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(member, updateMemberRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@LoginMember Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member,
        @Valid @RequestBody FavoriteRequest favoriteRequest) {
        favoriteService.addFavorite(member.getId(), favoriteRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok()
            .body(FavoriteResponse.of(favoriteService.getFavorites(member.getId())));
    }

    @GetMapping("/me/favorites/from/{sourceId}/to/{targetId}")
    public ResponseEntity<FavoriteExistResponse> hasFavorite(@LoginMember Member member,
        @PathVariable Long sourceId, @PathVariable Long targetId) {
        return ResponseEntity.ok()
            .body(new FavoriteExistResponse(
                favoriteService.hasFavorite(member.getId(), sourceId, targetId)));
    }

    @DeleteMapping("/me/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member,
        @PathVariable Long favoriteId) {
        favoriteService.removeFavoriteById(member.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
