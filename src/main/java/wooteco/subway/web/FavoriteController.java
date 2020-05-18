package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@LoginMember Member member, @RequestBody FavoriteRequest view) {
        favoriteService.createFavorite(member, view);
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<Favorite> favorites = favoriteService.findFavoritesByMember(member);
        return ResponseEntity.ok().body(FavoriteResponse.listOf(favorites));
    }
}
