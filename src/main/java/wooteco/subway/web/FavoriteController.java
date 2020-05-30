package wooteco.subway.web;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponses;
import wooteco.subway.web.member.LoginMember;

@RequestMapping(value = "/favorite/me")
@RestController
public class FavoriteController {
	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@RequestMapping(method = POST)
	public ResponseEntity<Void> createFavorite(@LoginMember Member member,
		@RequestBody FavoriteRequest favoriteRequest) {
		Favorite persistFavorite = favoriteService.createFavorite(favoriteRequest.toFavorite(member.getId()));
		return ResponseEntity.created(URI.create("/favorite/me/" + persistFavorite.getId())).build();
	}

	@RequestMapping(method = GET)
	public ResponseEntity<FavoriteResponses> getFavorites(@LoginMember Member member) {
		List<Favorite> favorites = favoriteService.getFavorites(member.getId());

		return ResponseEntity.ok(FavoriteResponses.of(FavoriteResponse.listOf(favorites)));
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public ResponseEntity<Void> deleteFavorite(@LoginMember Member member, @PathVariable Long id) {
		favoriteService.deleteFavorite(member.getId(), id);
		return ResponseEntity.ok().build();
	}
}
