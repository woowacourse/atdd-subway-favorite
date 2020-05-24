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

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> create(
            @LoginMember Member member,
            @RequestBody FavoriteRequest request
    ) {
        FavoriteResponse response = favoriteService.addFavorite(member.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showAll(
            @LoginMember Member member
    ) {
        List<FavoriteResponse> responses = favoriteService.showMyAllFavorites(member.getId());
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> remove(
            @LoginMember Member member,
            @PathVariable("id") Long id
    ) {
        favoriteService.removeFavorite(id);
        return ResponseEntity.noContent().build();
    }

}
