package wooteco.subway.web.member;

import static wooteco.subway.web.member.LoginMemberController.MEMBER_URI_WITH_AUTH;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RequestMapping(MEMBER_URI_WITH_AUTH)
@RestController
public class LoginMemberController {
	public static final String MEMBER_URI_WITH_AUTH = "/me";

	private MemberService memberService;

	public LoginMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping
	public ResponseEntity<Void> login(@RequestBody LoginRequest param) {
		String token = memberService.createToken(param);
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "bearer " + token).build();
	}

	@GetMapping
	public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember Member member) {
		return ResponseEntity.ok().body(MemberResponse.of(member));
	}

	@PutMapping
	public ResponseEntity<Void> updateMember(@LoginMember Member member,
		@RequestBody @Valid UpdateMemberRequest param) {
		memberService.updateMember(member, param);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<MemberResponse> deleteMember(@LoginMember Member member) {
		memberService.deleteMember(member);
		return ResponseEntity.noContent().build();
	}

	@GetMapping({"/basic", "/session", "/bearer"})
	public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
		return ResponseEntity.ok().body(MemberResponse.of(member));
	}
}
