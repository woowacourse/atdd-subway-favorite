package wooteco.subway.web.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberDetailResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.member.interceptor.IsAuth;
import wooteco.subway.web.member.resolver.LoginMember;

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
        return ResponseEntity.ok().body(MemberDetailResponse.of(member));
    }

}
