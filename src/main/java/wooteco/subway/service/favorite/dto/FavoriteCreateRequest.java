package wooteco.subway.service.favorite.dto;

public class FavoriteCreateRequest {
	private String source;
	private String target;

	private FavoriteCreateRequest() {
	}

	public FavoriteCreateRequest(String source, String target) {
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
