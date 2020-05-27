package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.favorite.FavoriteService;
import wooteco.subway.service.member.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

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
        favoriteService.createFavorite(member, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@LoginMember Member member) {
        List<FavoriteResponse> responses = favoriteService.findFavorites(member);
        return ResponseEntity
                .ok()
                .body(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @RequestBody FavoriteRequest request) {
        favoriteService.deleteFavorite(member, request);
        return ResponseEntity
                .noContent()
                .build();
    }
}
