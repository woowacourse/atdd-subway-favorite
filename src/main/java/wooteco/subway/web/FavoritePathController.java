package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoritePathService;
import wooteco.subway.service.station.dto.FavoriteRequest;
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
            @RequestBody FavoriteRequest favoriteRequest,
            @LoginMember Member member) {
        String startStationName = favoriteRequest.getStartStationName();
        String endStationName = favoriteRequest.getEndStationName();
        favoriteService.register(startStationName, endStationName, member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(
            @RequestBody FavoriteRequest favoriteRequest,
            @LoginMember Member member) {
        String startStationName = favoriteRequest.getStartStationName();
        String endStationName = favoriteRequest.getEndStationName();
        favoriteService.delete(startStationName, endStationName, member);
        return ResponseEntity.noContent().build();

    }
}
