package wooteco.subway.web.member;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

@RestController
@RequestMapping("/me/favorites")
public class FavoriteController {
    private MemberService memberService;

    public FavoriteController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return
            ResponseEntity
                .ok(memberService.findAllFavoritesByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(@LoginMember Member member, @RequestBody FavoriteRequest favoriteRequest) {
        memberService.saveFavorite(member.getId(), favoriteRequest);
        String sourceStationName = memberService.findStationNameById(
            favoriteRequest.getSourceStationId());
        String targetStationName = memberService.findStationNameById(
            favoriteRequest.getTargetStationId());
        return
            ResponseEntity
                .created(URI.create(
                    String.format("/paths?source=%s&target=%s&type=DISTANCE"
                        , sourceStationName
                        , targetStationName)))
                .build();
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable final Long favoriteId) {
        memberService.deleteFavorite(member, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
