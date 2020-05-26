package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody FavoriteCreateRequest request) {
        favoriteService.createFavorite(member.getId(), request);
        return ResponseEntity
                .created(URI.create("/favorites/" + member.getId()))
                .build();
    }
}
