package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.favorite.dto.FavoritePathsResponse;
import wooteco.subway.web.dto.FavoritePathRequest;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/me")
	public ResponseEntity<Void> registerFavoritePath(@LoginMember Member member,
	                                                 @RequestBody FavoritePathRequest request) {
		Long pathId = favoriteService.registerPath(member, request.getSource(), request.getTarget());
		return ResponseEntity.created(URI.create("/favorite/" + pathId)).build();
	}

	@GetMapping("/me")
	public ResponseEntity<FavoritePathsResponse> retrieveFavoritePath(@LoginMember Member member) {
		List<FavoritePathResponse> favoritePathResponse = favoriteService.retrievePath(member);
		FavoritePathsResponse favoritePathsResponse = new FavoritePathsResponse(favoritePathResponse);
		return ResponseEntity.ok(favoritePathsResponse);
	}

	@DeleteMapping("/me/{id}")
	public ResponseEntity<Void> deleteFavoritePath(@LoginMember Member member, @PathVariable Long id) {
		favoriteService.deletePath(member, id);
		return ResponseEntity.noContent().build();
	}
}
