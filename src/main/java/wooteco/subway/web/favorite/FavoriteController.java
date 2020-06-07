package wooteco.subway.web.favorite;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteRequest;
import wooteco.subway.service.favorite.FavoriteResponse;
import wooteco.subway.service.favorite.FavoriteResponses;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.web.member.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @LoginMember Member member) {
        favoriteService.save(member, request.toFavoriteStation(member));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponses> showAll(@LoginMember Member member) {
        FavoriteResponses response = favoriteService.findAll(member);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
