package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import javax.validation.Valid;

@RestController
public class LoginMemberController {
	private static final String TOKEN_BEARER = "bearer";

	private final MemberService memberService;

	public LoginMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/oauth/token")
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest param) {
		String token = memberService.createToken(param);
		return ResponseEntity.ok().body(new TokenResponse(token, TOKEN_BEARER));
	}

	@GetMapping("/me")
	public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
		return ResponseEntity.ok().body(MemberResponse.of(member));
	}

	@PutMapping("/me")
	public ResponseEntity<Void> updateMemberOfMineBasic(@LoginMember Member member,
														@RequestBody UpdateMemberRequest updateMemberRequest) {
		memberService.updateMember(member.getId(), updateMemberRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteMemberOfMineBasic(@LoginMember Member member) {
		memberService.deleteMember(member.getId());
		return ResponseEntity.noContent().build();
	}
}
