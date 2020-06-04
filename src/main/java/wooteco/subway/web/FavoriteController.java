package wooteco.subway.web;

import static wooteco.subway.web.FavoriteController.*;

import java.net.URI;

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
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

@RequestMapping(FAVORITE_URI)
@RestController
public class FavoriteController {
	public static final String FAVORITE_URI = "/members/favorites";

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<Void> addFavorite(@LoginMember Member member,
		@RequestBody FavoriteCreateRequest createRequest) {
		FavoriteResponse response = favoriteService.addFavorite(member, createRequest);
		return ResponseEntity.created(URI.create(FAVORITE_URI + "/" + response.getId())).build();
	}

	@GetMapping
	public ResponseEntity<FavoriteListResponse> findFavorites(@LoginMember Member member) {
		return ResponseEntity.ok(favoriteService.findFavorites(member));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFavorite(@LoginMember Member member,
		@PathVariable("id") Long favoriteId) {
		favoriteService.deleteFavorite(member, favoriteId);
		return ResponseEntity.noContent().build();
	}
}
