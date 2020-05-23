package wooteco.subway.web.member;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
@RequestMapping("/me")
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest view) {
        memberService.createMember(view.toMember());
        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @PutMapping
    public ResponseEntity<Void> editMemberOfMineBasic(@LoginMember Member member,
        @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(member.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMemberOfMineBasic(@LoginMember Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
        return
            ResponseEntity
                .ok(memberService.findAllFavoritesByMemberId(member.getId()));
    }

    @PostMapping("/favorites")
    public ResponseEntity addFavorite(@LoginMember Member member,
        @RequestBody FavoriteRequest favoriteRequest) {

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

    @DeleteMapping("/favorites")
    public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @RequestBody FavoriteRequest favoriteRequest) {
        memberService.deleteFavorite(member.getId(), favoriteRequest.getSourceStationId(), favoriteRequest
            .getTargetStationId());
        return ResponseEntity.noContent().build();
    }
}
