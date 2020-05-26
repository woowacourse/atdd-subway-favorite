package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
	private String source;
	private String target;

	private FavoriteResponse() {
	}

	public FavoriteResponse(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
