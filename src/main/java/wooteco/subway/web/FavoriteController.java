package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.argumentresolver.annotation.RequestMember;

@RestController
public class FavoriteController {
    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestMember Member member, @RequestBody CreateFavoriteRequest createFavoriteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FavoriteResponse(createFavoriteRequest.getSource(), createFavoriteRequest.getTarget()));
    }
}
