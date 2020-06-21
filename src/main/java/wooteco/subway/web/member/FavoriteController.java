package wooteco.subway.web.member;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

/**
 *    즐겨찾기 컨트롤러 클래스입니다.
 *
 *    @author HyungJu An
 */

@RestController
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(final FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorites")
	public ResponseEntity<Void> addFavorite(@LoginMember Member member, @RequestBody @Valid FavoriteRequest favoriteRequest) {

		favoriteService.createFavorite(member, favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/")).build();
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Member member) {
		return ResponseEntity.ok().body(favoriteService.getFavorites(member));
	}

	@DeleteMapping("/favorites/{favoriteId}")
	public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long favoriteId) {
		favoriteService.deleteFavorite(member, favoriteId);
		return ResponseEntity.noContent().build();
	}
}
