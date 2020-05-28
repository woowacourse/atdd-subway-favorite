package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Boolean>> ifFavoriteExists(@LoginMember Member member, @RequestParam String source, @RequestParam String destination) {
        Boolean ifExists = favoriteService.ifFavoriteExist(new FavoriteRequest(source, destination), member);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", ifExists);
        return ResponseEntity.ok().body(result);
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
