package wooteco.subway.web.member;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteExistenceResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
@RequestMapping("/members/my-info")
public class MyInfoController {
    private final MemberService memberService;
    private final FavoriteService favoriteService;

    public MyInfoController(MemberService memberService, FavoriteService favoriteService) {
        this.memberService = memberService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member request) {
        return ResponseEntity.ok().body(MemberResponse.of(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateMember(@LoginMember Member request,
        @Valid @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(request.getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@LoginMember Member request) {
        memberService.deleteMember(request.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites/existsPath")
    public ResponseEntity<FavoriteExistenceResponse> isExistFavoritePath(@LoginMember Member member,
        @RequestParam Long sourceId, @RequestParam Long targetId) {
        FavoriteExistenceResponse response = favoriteService.hasFavoritePath(member, sourceId, targetId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@LoginMember Member member,
        @Valid @RequestBody FavoriteRequest favoriteRequest) {
        favoriteService.addFavorite(member, favoriteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/favorites/source/{sourceId}/target/{targetId}")
    public ResponseEntity<Void> removeFavorite(@LoginMember Member member,
        @PathVariable Long sourceId, @PathVariable Long targetId) {
        favoriteService.removeFavorite(member, sourceId, targetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<FavoriteResponse> response = favoriteService.getFavorites(member);
        return ResponseEntity.ok(response);
    }
}
