package wooteco.subway.web.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.station.StationService;
import wooteco.subway.web.member.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final StationService stationService;

    public FavoriteController(FavoriteService favoriteService, StationService stationService) {
        this.favoriteService = favoriteService;
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @LoginMember Member member) {
        Station source = stationService.findByName(request.getSource());
        Station target = stationService.findByName(request.getTarget());
        favoriteService.save(member, new FavoriteStation(member.getId(), source.getId(), target.getId()));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FavoritesResponse> showAll(@LoginMember Member member) {
        final FavoritesResponse result = favoriteService.findAll(member);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@LoginMember Member member,
                                       @RequestParam("source") Long source, @RequestParam("target") Long target) {
        favoriteService.delete(member, source, target);
        return ResponseEntity.ok().build();
    }
}
