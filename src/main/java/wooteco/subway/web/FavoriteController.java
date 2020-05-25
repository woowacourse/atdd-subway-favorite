package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.acceptance.favorite.dto.FavoritePathResponse;
import wooteco.subway.acceptance.favorite.dto.StationPathResponse;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.FavoriteService;
import wooteco.subway.web.dto.FavoritePathRequest;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorite/me")
	public ResponseEntity<Void> registerFavoritePath(@LoginMember Member member,
	                                                 @RequestBody FavoritePathRequest request) {
		favoriteService.registerPath(member, request.getSource(), request.getTarget());
		return ResponseEntity.created(URI.create("/favorite/1")).build();
	}

	@GetMapping("/favorite/me")
	public ResponseEntity<FavoritePathResponse> retrieveFavoritePath(@LoginMember Member member) {
		List<StationPathResponse> stationPathResponses = favoriteService.retrievePath(member);
		FavoritePathResponse favoritePathResponse = new FavoritePathResponse(stationPathResponses);
		return ResponseEntity.ok(favoritePathResponse);
	}

	@DeleteMapping("/favorite/me/{id}")
	public ResponseEntity<Void> deleteFavoritePath(@LoginMember Member member, @PathVariable Long id) {
		favoriteService.deletePath(member, id);
		return ResponseEntity.noContent().build();
	}
}
