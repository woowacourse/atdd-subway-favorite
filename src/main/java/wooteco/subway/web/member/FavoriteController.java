package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FavoriteController {
    private MemberService memberService;

    public FavoriteController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        favoriteResponses.add(new FavoriteResponse("강남역", "도곡역"));
        favoriteResponses.add(new FavoriteResponse("선릉역", "도곡역"));

        return ResponseEntity.ok().body(favoriteResponses);
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody @Valid FavoriteRequest favoriteRequest) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @RequestBody @Valid FavoriteRequest favoriteRequest) {
        return ResponseEntity.noContent().build();
    }
}
