package wooteco.subway.common;

public enum AuthorizationType {

	BEARER("bearer");

	private String prefix;

	AuthorizationType(String prefix) {
		this.prefix = prefix;
	}

	public String combineToken(String token) {
		return prefix + " " + token;
	}

	public String getPrefix() {
		return prefix;
	}
}
