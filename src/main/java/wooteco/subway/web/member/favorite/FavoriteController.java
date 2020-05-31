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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members/{id}/favorites")
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> addFavorite(
			@PathVariable Long id,
			@RequestBody @Valid AddFavoriteRequest param,
			@LoginMember Member member
	) {
		Favorite favorite = favoriteService.addFavorite(id, param.getSourceId(), param.getTargetId());
		String createUri = "/members/" + id + "/favorites/source/" + param.getSourceId() + "/target/" + param.getTargetId();
		return ResponseEntity.created(URI.create(createUri))
				.body(FavoriteResponse.of(id, favorite));
	}

	@GetMapping
	public ResponseEntity<FavoritesResponse> readFavorites(
			@PathVariable Long id,
			@LoginMember Member member
	) {
		List<FavoriteDetail> favoriteDetails = favoriteService.readFavorites(id);

		return ResponseEntity.ok()
				.body(new FavoritesResponse(favoriteDetails));
	}

	@DeleteMapping("/source/{sourceId}/target/{targetId}")
	public ResponseEntity<Void> removeFavorite(
			@PathVariable Long id,
			@PathVariable Long sourceId,
			@PathVariable Long targetId,
			@LoginMember Member member
	) {
		favoriteService.removeFavorite(id, sourceId, targetId);
		return ResponseEntity.ok()
				.build();
	}
}
