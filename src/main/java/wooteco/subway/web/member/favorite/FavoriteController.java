package wooteco.subway.web.member.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.FavoriteDetail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.favorite.FavoriteService;
import wooteco.subway.service.member.favorite.dto.AddFavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.favorite.dto.FavoritesResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
public class FavoriteController {
	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/{id}/favorites")
	public ResponseEntity<FavoriteResponse> addFavorite(
			@PathVariable Long id,
			@RequestBody AddFavoriteRequest param,
			@LoginMember Member member
	) {
		member.validateId(id);
		Favorite favorite = favoriteService.addFavorite(id, param.getSourceId(), param.getTargetId());
		String createUri = "/members/" + id + "/favorites/source/" + param.getSourceId() + "/target/" + param.getTargetId();
		return ResponseEntity.created(URI.create(createUri))
				.body(FavoriteResponse.of(id, favorite));
	}

	@GetMapping("/{id}/favorites")
	public ResponseEntity<FavoritesResponse> readFavorites(
			@PathVariable Long id,
			@LoginMember Member member
	) {
		member.validateId(id);
		List<FavoriteDetail> favoriteDetails = favoriteService.readFavorites(id);

		return ResponseEntity.ok()
				.body(new FavoritesResponse(favoriteDetails));
	}

	@DeleteMapping("/{memberId}/favorites/source/{sourceId}/target/{targetId}")
	public ResponseEntity<Void> removeFavorite(
			@PathVariable Long memberId,
			@PathVariable Long sourceId,
			@PathVariable Long targetId,
			@LoginMember Member member
	) {
		member.validateId(memberId);
		favoriteService.removeFavorite(memberId, sourceId, targetId);
		return ResponseEntity.ok()
				.build();
	}
}
