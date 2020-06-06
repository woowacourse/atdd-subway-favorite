package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Member {
	@Id
	private final Long id;
	private final String email;
	private final String name;
	private final String password;
	@Embedded.Empty
	private final Favorites favorites;

	Member(final Long id, final String email, final String name, final String password,
		final Favorites favorites) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.favorites = favorites;
	}

	public static Member of(final String email, final String name, final String password) {
		return new Member(null, email, name, password, Favorites.of(new HashSet<>()));
	}

	public Member withId(final Long id) {
		return new Member(id, this.email, this.name, this.password, this.favorites);
	}

	public Member update(String name, String password) {
		if (StringUtils.isBlank(name)) {
			name = this.name;
		}
		if (StringUtils.isBlank(password)) {
			password = this.password;
		}
		return new Member(this.id, this.email, name, password, this.favorites);
	}

	public Member addFavorite(final Favorite favorite) {
		return new Member(this.id, this.email, this.name, this.password, this.favorites.add(favorite));
	}

	public Member deleteFavorite(final Favorite favorite) {
		return new Member(this.id, this.email, this.name, this.password, this.favorites.delete(favorite));
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

	public Set<Favorite> getFavorites() {
		return favorites.getFavorites();
	}
}
