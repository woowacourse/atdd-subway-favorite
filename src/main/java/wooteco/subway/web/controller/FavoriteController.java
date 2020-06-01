package wooteco.subway.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.prehandler.IsAuth;
import wooteco.subway.web.prehandler.LoginMember;
import wooteco.subway.web.service.favorite.FavoriteService;
import wooteco.subway.web.service.favorite.dto.FavoriteRequest;
import wooteco.subway.web.service.favorite.dto.FavoriteResponse;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @IsAuth
    @PostMapping("/favorites")
    public ResponseEntity<Void> create(@LoginMember Member member,
        @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse persistFavorite = favoriteService.create(member, favoriteRequest);
        return ResponseEntity.created(URI.create(
            "/members/" + persistFavorite.getMemberId() + "/favorites/" + persistFavorite.getId()))
            .build();
    }

    @IsAuth
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> delete(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.delete(member, id);
        return ResponseEntity.noContent().build();
    }
}
