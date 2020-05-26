package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<FavoriteResponse> favoriteResponses = favoriteService.showFavorites(member);

        return ResponseEntity.ok().body(favoriteResponses);
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody @Valid FavoriteRequest favoriteRequest) {
        favoriteService.createFavorite(favoriteRequest, member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @RequestBody @Valid FavoriteRequest favoriteRequest) {
        favoriteService.removeFavorite(favoriteRequest, member);
        return ResponseEntity.noContent().build();
    }
}
