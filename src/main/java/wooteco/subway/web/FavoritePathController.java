package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoritePathService;
import wooteco.subway.service.station.dto.FavoriteRegisterRequest;
import wooteco.subway.web.member.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {

    private FavoritePathService favoriteService;

    public FavoritePathController(FavoritePathService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> registerFavorite(
            @RequestBody FavoriteRegisterRequest favoriteRegisterRequest,
            @LoginMember Member member) {
        String startStationName = favoriteRegisterRequest.getStartStationName();
        String endStationName = favoriteRegisterRequest.getEndStationName();
        favoriteService.register(startStationName, endStationName, member);
        return ResponseEntity.ok().build();
    }
}
