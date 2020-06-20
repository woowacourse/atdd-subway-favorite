package wooteco.subway.web.member;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	private final MemberService memberService;
	private final FavoriteService favoriteService;

	public FavoriteController(MemberService memberService, FavoriteService favoriteService) {
		this.memberService = memberService;
		this.favoriteService = favoriteService;
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
		return ResponseEntity.ok(favoriteService.findFavorites(member));
	}

	@PostMapping
	public ResponseEntity<Void> addFavorite(@LoginMember Member member,
		@RequestBody FavoriteRequest favoriteRequest) {
		Long favoriteId = favoriteService.add(member, favoriteRequest);

		return
			ResponseEntity
				.created(
					URI.create("/favorites/" + favoriteId))
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable("id") Long favoriteId) {
		favoriteService.delete(member, favoriteId);
		return ResponseEntity.ok().build();
	}
}
