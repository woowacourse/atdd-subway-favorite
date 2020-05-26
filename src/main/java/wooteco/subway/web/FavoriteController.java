package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RequestMapping("/me/favorites")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.createFavorite(member, request);
        return ResponseEntity
                .created(URI.create("/me/favorites/" + response.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@LoginMember Member member) {
        List<FavoriteResponse> responses = favoriteService.findFavorites(member);
        return ResponseEntity
                .ok()
                .body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
