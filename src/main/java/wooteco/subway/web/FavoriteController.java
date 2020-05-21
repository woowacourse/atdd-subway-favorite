package wooteco.subway.web;

import java.net.URI;
import java.util.Arrays;
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
import wooteco.subway.web.member.LoginMemberId;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMemberId Long memberId,
        @RequestBody FavoriteRequest request) {
        return ResponseEntity
            .created(URI.create("/favorites/1"))
            .build();
    }

    @GetMapping("/favorites/{source}/{target}")
    public ResponseEntity<Boolean> hasFavorite(@LoginMemberId Long memberId, @PathVariable Long source,
        @PathVariable Long target) {
        return ResponseEntity.ok(true);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> readFavorites(@LoginMemberId Long memberId) {
        return ResponseEntity
            .ok(Arrays.asList(new FavoriteResponse()));
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@LoginMemberId Long memberId,
        @PathVariable Long favoriteId) {
        return ResponseEntity.noContent().build();
    }
}
