package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.web.member.argumentresolver.annotation.CreateFavorite;
import wooteco.subway.web.member.argumentresolver.annotation.LoginMember;

import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@LoginMember Member member, @RequestBody CreateFavoriteRequest request) {
        Favorite present = favoriteService.save(request, member.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FavoriteResponse.of(present));
    }

    @GetMapping("/favorites")
    public ResponseEntity<FavoritesResponse> createFavorite(@LoginMember Member member) {
        List<FavoriteResponse> favorites = favoriteService.findAllByEmail(member.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new FavoritesResponse(favorites));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@CreateFavorite Favorite favorite) {
        favoriteService.deleteFavorite(favorite);
        return ResponseEntity
                .noContent()
                .build();
    }
}
