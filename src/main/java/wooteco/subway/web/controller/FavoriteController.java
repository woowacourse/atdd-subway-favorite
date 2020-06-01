package wooteco.subway.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.web.LoginMember;

@RequestMapping("/me/favorites")
@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<Void> createFavorite(@LoginMember Member member, @RequestBody FavoriteRequest request) {
		favoriteService.createFavorite(member, request);
		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> showFavorites(@LoginMember Member member) {
		List<FavoriteResponse> responses = favoriteService.findFavorites(member);
		return ResponseEntity
			.ok()
			.body(responses);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @RequestBody FavoriteRequest request) {
		favoriteService.deleteFavorite(member, request);

		return ResponseEntity
			.noContent()
			.build();
	}

}
