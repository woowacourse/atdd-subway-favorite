package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponses;
import wooteco.subway.web.member.LoginMember;

import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        FavoriteResponses favoriteResponses = favoriteService.findAllFavoriteResponses(member.getId());
        return ResponseEntity.ok(favoriteResponses.getFavoriteResponses());
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<Void> addFavorite(@LoginMember Member member, @RequestBody FavoriteCreateRequest request) {
        favoriteService.createFavorite(member.getId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok().build();
    }
}
