package wooteco.subway.web.favorite;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteRequest;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.web.member.LoginMember;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @LoginMember Member member){
        FavoriteStation station = favoriteService.save(request.toFavoriteStation(member));
        return ResponseEntity
            .created(URI.create("/favorites/" + station.getId()))
            .build();
    }
}
