package wooteco.subway.service.favorite.dto;

public class FavoriteDeleteRequest {
	private String source;
	private String target;

	private FavoriteDeleteRequest() {
	}

	public FavoriteDeleteRequest(String source, String target) {
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
