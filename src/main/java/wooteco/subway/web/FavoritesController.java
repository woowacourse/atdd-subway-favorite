package wooteco.subway.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

@RequestMapping("members/favorites")
@RestController
public class FavoritesController {
    @PostMapping
    public ResponseEntity<Void> addFavorite(@LoginMember Member member,
            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return ResponseEntity.ok()
                .body(Arrays.asList(new FavoriteResponse(1L, "강남", 2L, "사당"),
                        new FavoriteResponse(3L, "강북", 9L, "제주도")));
    }
}
