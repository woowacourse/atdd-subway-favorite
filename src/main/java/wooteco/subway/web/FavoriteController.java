package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(member.getId());
        return ResponseEntity.ok(favoriteResponses);
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<Void> addFavorite(@LoginMember Member member, @RequestBody FavoriteCreateRequest request) {
        memberService.addFavorite(member.getId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long favoriteId) {
        memberService.deleteFavorite(member.getId(), favoriteId);
        return ResponseEntity.ok().build();
    }
}
