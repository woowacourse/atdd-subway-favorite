package wooteco.subway.service.member.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.domain.member.Favorite;

public class FavoriteResponse {

	private String source;
	private String target;

	public FavoriteResponse() {
	}

	public FavoriteResponse(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getSource(), favorite.getTarget());
	}

	public static List<FavoriteResponse> listOf(Set<Favorite> favorites) {
		return favorites.stream()
		                .map(FavoriteResponse::of)
		                .collect(Collectors.toList());
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

}
