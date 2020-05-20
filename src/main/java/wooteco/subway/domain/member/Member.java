package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

public class Member {
	@Id
	private final Long id;
	private final String email;
	private final String name;
	private final String password;

	Member(Long id, String email, String name, String password) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public static Member of(String email, String name, String password) {
		return new Member(null, email, name, password);
	}

	public Member withId(final Long id) {
		return new Member(id, this.email, this.name, this.password);
	}

	public Member update(String name, String password) {
		if (StringUtils.isBlank(name)) {
			name = this.name;
		}
		if (StringUtils.isBlank(password)) {
			password = this.password;
		}
		return new Member(this.id, this.email, name, password);
	}

	public boolean checkPassword(final String password) {
		return this.password.equals(password);
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

	public String getPassword() {
		return password;
	}
}
