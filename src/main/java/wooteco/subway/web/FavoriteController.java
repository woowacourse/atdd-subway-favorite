package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.web.member.argumentresolver.annotation.LoginMember;

@RestController
@RequestMapping("/auth/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping()
    public ResponseEntity<FavoriteResponse> createFavorite(@LoginMember Member member, @RequestBody CreateFavoriteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.createFavorite(member, request));
    }

    @GetMapping()
    public ResponseEntity<FavoritesResponse> getFavorite(@LoginMember Member member) {
        FavoritesResponse favoritesResponse = favoriteService.findAllByMemberId(member.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(favoritesResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
