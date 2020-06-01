package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.service.favorite.FavoritePathService;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.favorite.dto.FavoritePathsResponse;
import wooteco.subway.web.dto.FavoritePathRequest;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoritePathService favoritePathService;

    public FavoriteController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping("/favorite/me")
    public ResponseEntity<Void> registerFavoritePath(@LoginMember Member member,
        @RequestBody FavoritePathRequest request) {
        FavoritePath favoritePath = favoritePathService.registerPath(member, request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create(String.format("/favorite/%d", favoritePath.getId()))).build();
    }

    @GetMapping("/favorite/me")
    public ResponseEntity<FavoritePathsResponse> retrieveFavoritePath(@LoginMember Member member) {
        List<FavoritePathResponse> favoritePathResponse = favoritePathService.retrievePath(member);
        FavoritePathsResponse favoritePathsResponse = new FavoritePathsResponse(favoritePathResponse);
        return ResponseEntity.ok(favoritePathsResponse);
    }

    @DeleteMapping("/favorite/me/{id}")
    public ResponseEntity<Void> deleteFavoritePath(@LoginMember Member member, @PathVariable Long id) {
        favoritePathService.deletePath(member, id);
        return ResponseEntity.noContent().build();
    }
}
