package wooteco.subway.web.member;

import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

@RestController
public class LoginMemberController {

	private final MemberService memberService;

	public LoginMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/oauth/token")
	public ResponseEntity<TokenResponse> login(@RequestBody @Validated LoginRequest request) {
		TokenResponse jwtToken = memberService.createJwtToken(request);
		return ResponseEntity.ok().body(jwtToken);
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody @Validated LoginRequest request,
		HttpSession session) {
		if (!memberService.loginWithForm(request)) {
			throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
		}
		session.setAttribute("loginMemberEmail", request.getEmail());
		return ResponseEntity.ok().build();
	}

	@GetMapping({"/me/basic", "/me/session", "/me/bearer"})
	public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member request) {
		return ResponseEntity.ok().body(MemberResponse.of(request));
	}
}
