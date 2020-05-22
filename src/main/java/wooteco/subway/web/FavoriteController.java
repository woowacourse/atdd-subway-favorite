package wooteco.subway.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.service.favorite.FavoriteRequest;
import wooteco.subway.service.favorite.FavoriteResponse;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.web.member.LoginMemberId;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMemberId Long memberId,
        @RequestBody FavoriteRequest request) {
        Long id = favoriteService.addFavorite(memberId, request);

        return ResponseEntity
            .created(URI.create("/favorites/" + id))
            .build();
    }

    @GetMapping("/favorites/{source}/{target}")
    public ResponseEntity<FavoriteResponse> getFavorite(@LoginMemberId Long memberId,
        @PathVariable Long source,
        @PathVariable Long target) {
        FavoriteResponse favoriteResponse = favoriteService.getFavorite(memberId, source, target);

        return ResponseEntity.ok(favoriteResponse);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> readFavorites(@LoginMemberId Long memberId) {
        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorites(memberId);

        return ResponseEntity
            .ok(favoriteResponses);
    }

    @DeleteMapping("/favorites/{source}/{target}")
    public ResponseEntity<Void> deleteFavorite(@LoginMemberId Long memberId,
        @PathVariable Long source, @PathVariable Long target) {
        favoriteService.removeFavorite(memberId, source, target);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavoriteById(@LoginMemberId Long memberId,
        @PathVariable Long favoriteId) {
        favoriteService.removeFavoriteById(memberId, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
