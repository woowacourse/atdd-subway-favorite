package wooteco.subway.web.member.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.favorite.FavoriteService;
import wooteco.subway.service.member.favorite.dto.AddFavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.LoginMember;

import java.net.URI;

@RestController
public class FavoriteController {
	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/members/{id}/favorites")
	public ResponseEntity addFavorite(
			@PathVariable Long id,
			@RequestBody AddFavoriteRequest param,
			@LoginMember Member member
	) {
		member.validateId(id);
		Favorite favorite = favoriteService.addFavorite(id, param.getSourceId(), param.getTargetId());
		String createUri = "/members/" + id + "/favorites/sourceId/" + param.getSourceId() + "/targetId/" + param.getTargetId();
		return ResponseEntity.created(URI.create(createUri))
				.body(FavoriteResponse.of(id, favorite));
	}
}
