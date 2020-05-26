package wooteco.subway.web.member;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteExistenceResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.station.dto.StationResponse;

@RestController
@RequestMapping("/me")
public class MeController {
    private final MemberService memberService;
    private final FavoriteService favoriteService;

    public MeController(MemberService memberService, FavoriteService favoriteService) {
        this.memberService = memberService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member request) {
        return ResponseEntity.ok().body(MemberResponse.of(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateMember(@LoginMember Member request, @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(request.getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@LoginMember Member request) {
        memberService.deleteMember(request.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites/source/{sourceId}/target/{targetId}/existsPath")
    public ResponseEntity<FavoriteExistenceResponse> isExistFavoritePath(@LoginMember Member request,
        @PathVariable Long sourceId, @PathVariable Long targetId) {
        return ResponseEntity.ok(new FavoriteExistenceResponse(true));
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@LoginMember Member request,
        @RequestBody FavoriteRequest favoriteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/favorites/source/{sourceId}/target/{targetId}")
    public ResponseEntity<Void> removeFavorite(@LoginMember Member request,
        @PathVariable Long sourceId, @PathVariable Long targetId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<FavoriteResponse> favorites = favoriteService.getFavorites(member);
        return ResponseEntity.ok(favorites);
    }

    private FavoriteResponse createFavorite() {
        return new FavoriteResponse(new StationResponse(), new StationResponse());
    }
}
