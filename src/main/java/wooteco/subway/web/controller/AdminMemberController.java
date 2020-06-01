package wooteco.subway.web.controller;

import java.net.URI;

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
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RequestMapping("/admin/members")
@RestController
public class AdminMemberController {

	private final MemberService memberService;

	public AdminMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping
	public ResponseEntity<Void> createMember(@RequestBody MemberRequest view) {
		Member member = memberService.createMember(view.toMember());
		return ResponseEntity
			.created(URI.create("/admin/members/" + member.getId()))
			.build();
	}

	@GetMapping
	public ResponseEntity<MemberResponse> getMemberByEmail(@RequestParam String email) {
		Member member = memberService.findMemberByEmail(email);
		return ResponseEntity
			.ok()
			.body(MemberResponse.of(member));
	}

	@PutMapping("{id}")
	public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest param) {
		memberService.updateMember(id, param);
		return ResponseEntity
			.ok()
			.build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
		memberService.deleteMember(id);
		return ResponseEntity
			.noContent()
			.build();
	}

}
