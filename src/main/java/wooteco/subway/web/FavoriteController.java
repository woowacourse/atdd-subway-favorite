package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.web.member.LoginMember;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@LoginMember MemberResponse memberResponse,
        @RequestBody @Valid FavoriteRequest favoriteRequest) {
        Long favoriteId = favoriteService.create(memberResponse.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(
        @LoginMember MemberResponse memberResponse) {
        List<FavoriteResponse> responses = favoriteService.findAll(memberResponse.getId());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(@LoginMember MemberResponse memberResponse,
        @RequestBody @Valid FavoriteRequest favoriteRequest) {
        favoriteService.delete(memberResponse.getId(), favoriteRequest);
        return ResponseEntity.ok().build();
    }
}
