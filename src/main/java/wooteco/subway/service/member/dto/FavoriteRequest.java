package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

import wooteco.subway.domain.member.Favorite;

public class FavoriteRequest {

	@NotBlank(message = "출발역이 존재하지 않습니다.")
	private String source;

	@NotBlank(message = "도착역이 존재하지 않습니다.")
	private String target;

	public FavoriteRequest() {
	}

	public FavoriteRequest(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public Favorite toFavorite() {
		return new Favorite(source, target);
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

}
