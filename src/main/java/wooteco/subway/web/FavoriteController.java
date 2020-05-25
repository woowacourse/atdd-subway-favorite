package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.argumentresolver.annotation.CreateFavorite;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@CreateFavorite Favorite favorite) {
        Favorite present = favoriteService.save(favorite);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FavoriteResponse.of(present));
    }
}
