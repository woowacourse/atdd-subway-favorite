package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoritePathService;
import wooteco.subway.service.station.dto.FavoritePathRequest;
import wooteco.subway.service.station.dto.FavoritePathResponse;
import wooteco.subway.web.member.LoginMember;

import java.util.List;

@RestController
@RequestMapping("/favoritePaths")
public class FavoritePathController {

    private FavoritePathService favoriteService;

    public FavoritePathController(FavoritePathService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> showAll(@LoginMember Member member) {
        List<FavoritePathResponse> favoritePathResponses =
            this.favoriteService.findAllOf(member);
        return ResponseEntity.ok().body(favoritePathResponses);
    }

    @PostMapping
    public ResponseEntity<Void> registerFavorite(
            @RequestBody FavoritePathRequest favoritePathRequest,
            @LoginMember Member member) {
        String startStationName = favoritePathRequest.getStartStationName();
        String endStationName = favoritePathRequest.getEndStationName();
        favoriteService.register(startStationName, endStationName, member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(
            @RequestBody FavoritePathRequest favoritePathRequest,
            @LoginMember Member member) {
        String startStationName = favoritePathRequest.getStartStationName();
        String endStationName = favoritePathRequest.getEndStationName();
        favoriteService.delete(startStationName, endStationName, member);
        return ResponseEntity.noContent().build();
    }
}
