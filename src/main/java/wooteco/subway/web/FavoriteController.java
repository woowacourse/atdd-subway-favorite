package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;

@Controller
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/members/favorites")
    public ResponseEntity<Void> addFavorite(@LoginMember Member member,
                                            @RequestBody FavoriteCreateRequest createRequest) {
        FavoriteResponse response = favoriteService.addFavorite(member, createRequest);
        return ResponseEntity.created(URI.create("/members/favorites/" + response.getId())).build();
    }

    @GetMapping("/members/favorites")
    public ResponseEntity<FavoriteListResponse> findFavorites(@LoginMember Member member) {
        return ResponseEntity.ok(favoriteService.findFavorites(member));
    }

    @DeleteMapping("/members/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member,
                                               @PathVariable("id") Long favoriteId) {
        favoriteService.deleteFavorite(member, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
