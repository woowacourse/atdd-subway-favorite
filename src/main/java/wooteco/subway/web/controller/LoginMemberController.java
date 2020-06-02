package wooteco.subway.web.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.prehandler.IsAuth;
import wooteco.subway.web.prehandler.LoginMember;
import wooteco.subway.web.service.favorite.FavoriteService;
import wooteco.subway.web.service.favorite.dto.FavoriteDetailResponse;
import wooteco.subway.web.service.member.MemberService;
import wooteco.subway.web.service.member.dto.LoginRequest;
import wooteco.subway.web.service.member.dto.MemberDetailResponse;
import wooteco.subway.web.service.member.dto.MemberResponse;
import wooteco.subway.web.service.member.dto.TokenResponse;

@RestController
public class LoginMemberController {
    private final MemberService memberService;
    private final FavoriteService favoriteService;

    public LoginMemberController(MemberService memberService,
        FavoriteService favoriteService) {
        this.memberService = memberService;
        this.favoriteService = favoriteService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @IsAuth
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @IsAuth
    @GetMapping("/me/detail")
    public ResponseEntity<MemberDetailResponse> getMemberDetailOfMineBasic(@LoginMember Member member) {
        Set<FavoriteDetailResponse> responses = favoriteService.getAll(member);
        return ResponseEntity.ok().body(MemberDetailResponse.of(member, responses));
    }

    @IsAuth
    @GetMapping("/me/favorites")
    public ResponseEntity<Set<FavoriteDetailResponse>> getMemberFavorites(@LoginMember Member member) {
        Set<FavoriteDetailResponse> responses =  favoriteService.getAll(member);
        return ResponseEntity.ok().body(responses);
    }
}
