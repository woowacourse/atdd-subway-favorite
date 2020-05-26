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

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
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
        @RequestBody FavoriteRequest favoriteRequest) {
        memberService.addFavorite(member, favoriteRequest.toFavorite());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok().body(FavoriteResponse.of(memberService.getFavorites(member)));
    }

    @GetMapping("/me/favorites/from/{sourceId}/to/{targetId}")
    public ResponseEntity<FavoriteExistResponse> hasFavorite(@LoginMember Member member,
        @PathVariable Long sourceId, @PathVariable Long targetId) {
        return ResponseEntity.ok().body(new FavoriteExistResponse(memberService.hasFavorite(member, sourceId, targetId)));
    }

    @DeleteMapping("/me/favorites")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member,
        @RequestBody FavoriteRequest favoriteRequest) {
        memberService.removeFavorite(member, favoriteRequest.toFavorite());
        return ResponseEntity.noContent().build();
    }
}
