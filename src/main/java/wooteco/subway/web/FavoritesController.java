package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoritesService;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

import java.util.List;

@RequestMapping("members/favorites")
@RestController
public class FavoritesController {
    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(@LoginMember Member member,
            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        favoritesService.addFavorite(member, favoriteCreateRequest);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok()
                .body(favoritesService.getFavorites(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavorite(@LoginMember Member member, @PathVariable("id") Long favoriteId) {
        favoritesService.deleteFavorite(member, favoriteId);
        return ResponseEntity.noContent()
                .build();
    }
}
