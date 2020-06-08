package wooteco.subway.domain.member;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.Favorites;

public class Member {
	@Id
	private final Long id;
	private final String email;
	private final String name;
	private final String password;

	@Embedded.Empty
	private final Favorites favorites;

	Member(Long id, String email, String name, String password, Favorites favorites) {
		validateIsNotBlank(email);
		validateIsNotBlank(name);
		validateIsNotBlank(password);

		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.favorites = favorites;
	}

	private void validateIsNotBlank(String string) {
		if (StringUtils.isBlank(string)) {
			throw new IllegalArgumentException(
				"이메일, 이름, 패스워드에 널 값이나 빈 값, 공백만있는 값은 허용되지 않습니다.");
		}
	}

	public static Member of(String email, String name, String password) {
		return new Member(null, email, name, password, Favorites.empty());
	}

	public Member withId(Long id) {
		return new Member(id, this.email, this.name, this.password, this.favorites);
	}

	public Member updateNameAndPassword(String name, String password) {
		return new Member(this.id, this.email, name, password, this.favorites);
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	public Member addFavorite(Favorite favorite) {
		return new Member(this.id, this.email, this.name, this.password,
			favorites.add(favorite));
	}

	public Member removeFavorite(long sourceId, long targetId) {
		return new Member(this.id, this.email, this.name, this.password,
			favorites.remove(sourceId, targetId));
	}

	public Set<Long> getAllStationIds() {
		return favorites.getAllStationIds();
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

	public Favorites getFavorites() {
		return favorites;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Member member = (Member)o;
		return Objects.equals(id, member.id) &&
			Objects.equals(email, member.email) &&
			Objects.equals(name, member.name) &&
			Objects.equals(password, member.password) &&
			Objects.equals(favorites, member.favorites);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, name, password, favorites);
	}

	@Override
	public String toString() {
		return "Member{" +
			"id=" + id +
			", email='" + email + '\'' +
			", name='" + name + '\'' +
			", password='" + password + '\'' +
			", favorites=" + favorites +
			'}';
	}
}
