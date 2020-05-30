package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

public class Favorite {
	@Id
	private Long id;
	private Long memberId;
	private String source;
	private String target;

	public Favorite() {
	}

	public Favorite(Long memberId, String source, String target) {
		this.memberId = memberId;
		this.source = source;
		this.target = target;
	}

	public Favorite(Long id, Long memberId, String source, String target) {
		this.id = id;
		this.memberId = memberId;
		this.source = source;
		this.target = target;
	}

	public Long getId() {
		return id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
