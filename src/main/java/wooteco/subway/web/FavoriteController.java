package wooteco.subway.web;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.web.member.LoginMember;

@RestController
public class FavoriteController {
    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember MemberResponse memberResponse,
        @RequestBody @Valid FavoriteCreateRequest favoriteCreateRequest) {
        return ResponseEntity.created(URI.create("a")).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showFavorites(
        @LoginMember MemberResponse memberResponse) {
        return ResponseEntity.ok(Collections.singletonList(new FavoriteResponse("a", "b")));
    }
}
