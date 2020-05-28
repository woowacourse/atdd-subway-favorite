package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.TokenResponse;

@RestController
public class OAuthController {
	private MemberService memberService;

	public OAuthController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/oauth/token")
	public ResponseEntity<TokenResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
		String token = memberService.createToken(loginRequest);
		return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
	}
}
