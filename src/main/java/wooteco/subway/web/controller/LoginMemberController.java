package wooteco.subway.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.LoginMember;

@RestController
public class LoginMemberController {

	private static final String BEARER_TOKEN_TYPE = "Bearer";

	private final MemberService memberService;

	public LoginMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest param) {
		String token = memberService.createToken(param);
		return ResponseEntity
			.ok()
			.body(new TokenResponse(token, BEARER_TOKEN_TYPE));
	}

	@PostMapping("/members")
	public ResponseEntity<Void> createMember(@RequestBody MemberRequest view) {
		memberService.createMember(view.toMember());
		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping("/me")
	public ResponseEntity<MemberResponse> getMember(@LoginMember Member member) {
		return ResponseEntity
			.ok()
			.body(MemberResponse.of(member));
	}

	@PutMapping("/me")
	public ResponseEntity<Void> updateMember(@LoginMember Member member, @RequestBody UpdateMemberRequest request) {
		memberService.updateMember(member, request);
		return ResponseEntity
			.noContent()
			.build();
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteMember(@LoginMember Member member) {
		memberService.deleteMember(member);
		return ResponseEntity
			.noContent()
			.build();
	}

}
