package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/me/favorites")
@RestController
public class FavoriteController {
    private List<FavoriteResponse> responses = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = new FavoriteResponse((long) (responses.size() + 1), request.getSource(), request.getTarget());
        responses.add(response);
        return ResponseEntity
                .created(URI.create("/me/favorites/" + response.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@LoginMember Member member) {
        return ResponseEntity
                .ok()
                .body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
        responses.remove((int) (id - 1));
        return ResponseEntity
                .noContent()
                .build();
    }
}
