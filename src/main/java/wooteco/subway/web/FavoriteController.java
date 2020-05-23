package wooteco.subway.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	private MemberService memberService;

	public FavoriteController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> get(@LoginMember Member member) {
		return
			ResponseEntity
				.ok(memberService.findAllFavoritesByMemberId(member.getId()));
	}

	@PostMapping
	public ResponseEntity<Void> create(@LoginMember Member member,
		@RequestBody FavoriteRequest favoriteRequest) {
		memberService.saveFavorite(member.getId(), favoriteRequest);
		return
			ResponseEntity
				.created(URI.create("/favorite/" + 1))
				.build();
	}
}
