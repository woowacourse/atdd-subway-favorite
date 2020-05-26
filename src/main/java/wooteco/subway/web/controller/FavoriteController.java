package wooteco.subway.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.prehandler.Auth;
import wooteco.subway.web.prehandler.IsAuth;
import wooteco.subway.web.prehandler.LoginMember;
import wooteco.subway.web.service.favorite.FavoriteService;
import wooteco.subway.web.service.favorite.dto.FavoriteRequest;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @IsAuth(isAuth = Auth.AUTH)
    @PostMapping("/favorites")
    public ResponseEntity<Void> create(@LoginMember Member member,
        @RequestBody FavoriteRequest favoriteRequest) {
        Member persistMember = favoriteService.addToMember(member, favoriteRequest);
        return ResponseEntity.created(URI.create("/members/" + persistMember.getId() + "/favorites"))
            .build();
    }

    @IsAuth(isAuth = Auth.AUTH)
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> delete(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
