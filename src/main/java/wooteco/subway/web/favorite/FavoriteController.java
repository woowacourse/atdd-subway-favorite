package wooteco.subway.web.favorite;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.auth.RequireAuth;
import wooteco.subway.web.member.resolver.LoginMember;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @RequireAuth
    @PostMapping("/members/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member,
        @Validated @RequestBody FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteService.createFavorite(member, favoriteRequest);

        return ResponseEntity
            .created(URI.create("/members/favorites/" + favorite.getId()))
            .build();
    }

    @RequireAuth
    @GetMapping("/members/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<FavoriteResponse> favorites = favoriteService.getFavorites(member);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(favorites);
    }

    @RequireAuth
    @DeleteMapping("/members/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity.noContent().build();
    }
}
