package wooteco.subway.web.favorite;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteRequest;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.FavoritesResponse;
import wooteco.subway.web.member.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @LoginMember Member member){
        favoriteService.save(member, request.toFavoriteStation(member));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FavoritesResponse> showAll(@LoginMember Member member) {
        final FavoritesResponse result = favoriteService.findAll(member);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@LoginMember Member member,
        @RequestParam("source") String source, @RequestParam("target") String target){
        favoriteService.delete(member, source, target);
        return ResponseEntity.ok().build();
    }
}
