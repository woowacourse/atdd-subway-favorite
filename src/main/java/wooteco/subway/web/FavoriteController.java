package wooteco.subway.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
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
import wooteco.subway.service.favorite.dto.FavoriteResponses;
import wooteco.subway.web.member.LoginMember;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorite/me")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member,
        @RequestBody FavoriteRequest favoriteRequest) {
        Favorite persistFavorite = favoriteService.createFavorite(member.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorite/me/" + persistFavorite.getId())).build();
    }

    @GetMapping("/favorite/me")
    public ResponseEntity<FavoriteResponses> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok(FavoriteResponses.of(favoriteService.getFavoriteResponse(member.getId())));
    }

    @DeleteMapping("/favorite/me/{id}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member.getId(), id);
        return ResponseEntity.ok().build();
    }
}
