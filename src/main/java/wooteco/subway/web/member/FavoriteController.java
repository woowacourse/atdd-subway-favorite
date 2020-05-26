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
    public ResponseEntity<List<FavoriteResponse>> getAllFavorites(@LoginMember Member member) {
        List<FavoriteResponse> favoriteResponses = favoriteService.showAllFavorites(member);

        return ResponseEntity.ok().body(favoriteResponses);
    }

    @GetMapping("/favorites/exists")
    public ResponseEntity<Boolean> ifFavoriteExists(@LoginMember Member member, @RequestParam String source, @RequestParam String destination) {
        Boolean ifExists = favoriteService.findFavorite(member, source, destination);
        return ResponseEntity.ok().body(ifExists);
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
