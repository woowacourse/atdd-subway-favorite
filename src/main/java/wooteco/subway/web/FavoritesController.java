package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoritesService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.List;

@RequestMapping("/members")
@RestController
public class FavoritesController {
    private final FavoritesService favoritesService;
    private final MemberService memberService;

    public FavoritesController(FavoritesService favoritesService, MemberService memberService) {
        this.favoritesService = favoritesService;
        this.memberService = memberService;
    }

    @PostMapping("/{id}/favorites")
    public ResponseEntity<Void> addFavorite(@PathVariable("id") Long memberId,
                                            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        Member member = memberService.findMemberById(memberId);
        favoritesService.addFavorite(member, favoriteCreateRequest);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@PathVariable("id") Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return ResponseEntity.ok()
                .body(favoritesService.getFavorites(member));
    }

    @DeleteMapping("/{memberId}/favorites/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable("memberId") Long memberId, @PathVariable("favoriteId") Long favoriteId) {
        Member member = memberService.findMemberById(memberId);
        favoritesService.deleteFavorite(member, favoriteId);
        return ResponseEntity.noContent()
                .build();
    }
}
