package woowa.bossdog.subway.web.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.service.favorite.FavoriteService;
import woowa.bossdog.subway.service.favorite.dto.FavoriteRequest;
import woowa.bossdog.subway.service.favorite.dto.FavoriteResponse;
import woowa.bossdog.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteApiController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> create(
            @LoginMember Member member,
            @RequestBody FavoriteRequest request
    ) {
        final FavoriteResponse response = favoriteService.createFavorite(member, request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> list(@LoginMember Member member) {
        final List<FavoriteResponse> responses = favoriteService.listFavorites(member);
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @LoginMember Member member,
            @PathVariable("id") Long id
    ) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity.noContent().build();
    }
}
