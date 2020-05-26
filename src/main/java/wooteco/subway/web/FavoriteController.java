package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites/me")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member,
                                                         @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        favoriteService.create(member, favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/favorites/me")).build();
    }

    @GetMapping("/favorites/me")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok().body(favoriteService.find(member));
    }

    @DeleteMapping("/favorites/me")
    public ResponseEntity<MemberResponse> deleteFavorite(@LoginMember Member member,
                                                         @RequestBody FavoriteDeleteRequest favoriteDeleteRequest) {
        favoriteService.delete(member, favoriteDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
