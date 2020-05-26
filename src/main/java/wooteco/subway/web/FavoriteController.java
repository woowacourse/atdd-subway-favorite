package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
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
    public ResponseEntity<FavoriteResponse> createFavorite(@CreateFavorite Favorite favorite) {
        Favorite present = favoriteService.save(favorite);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FavoriteResponse.of(present));
    }

    @GetMapping("/favorites")
    public ResponseEntity<FavoritesResponse> createFavorite(@LoginMember Member member) {
        System.out.println(member.getEmail() + "메메메메메멤");
        List<FavoriteResponse> favorites = favoriteService.findAllByEmail(member.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new FavoritesResponse(favorites));
    }
}
