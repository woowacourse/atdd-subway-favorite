package wooteco.subway.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoritesService;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

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
                .body(favoritesService.getFavoritesBy(member));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@LoginMember Member member,
            @RequestBody FavoriteDeleteRequest favoriteDeleteRequest) {
        favoritesService.deleteFavorite(member, favoriteDeleteRequest);
        return ResponseEntity.noContent()
                .build();
    }
}
