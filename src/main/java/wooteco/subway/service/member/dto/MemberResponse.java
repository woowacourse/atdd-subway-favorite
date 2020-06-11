package wooteco.subway.service.member.dto;

import java.util.Objects;
import wooteco.subway.domain.member.Member;

public class MemberResponse {

	private Long id;
	private String email;
	private String name;

	private MemberResponse() {
	}

	public MemberResponse(Long id, String email, String name) {
		this.id = id;
		this.email = email;
		this.name = name;
	}

	public static MemberResponse of(Member member) {
		return new MemberResponse(member.getId(), member.getEmail(), member.getName());
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MemberResponse that = (MemberResponse) o;
		return Objects.equals(id, that.id) &&
			Objects.equals(email, that.email) &&
			Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, name);
	}
}
