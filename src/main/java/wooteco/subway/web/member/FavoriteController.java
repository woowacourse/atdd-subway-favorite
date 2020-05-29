package wooteco.subway.web.member;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Void> addFavorite(@LoginMember Member member,
		@RequestBody @Valid FavoriteRequest favoriteRequest) {
		favoriteService.createFavorite(member, favoriteRequest.toFavorite());
		return ResponseEntity.created(
			URI.create("/favorites/")).build();
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> retrieveFavorites(@LoginMember Member member) {
		List<FavoriteResponse> favoriteResponses = FavoriteResponse.listOf(
			favoriteService.retrieveStationsBy(member.getFavorites()));

		return ResponseEntity.ok().body(favoriteResponses);
	}

	@DeleteMapping("/favorites")
	public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, FavoriteRequest favoriteRequest) {
		favoriteService.deleteFavorite(member, favoriteRequest.toFavorite());
		return ResponseEntity.noContent().build();
	}
}
