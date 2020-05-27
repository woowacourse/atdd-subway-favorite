package wooteco.subway.domain.member;

import static java.util.stream.Collectors.*;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import wooteco.subway.service.station.DuplicateFavoriteException;

public class Member {
	@Id
	private final Long id;
	private final String email;
	private final String name;
	private final String password;
	private final Set<Favorite> favorites;

	Member(Long id, String email, String name, String password,
		final Set<Favorite> favorites) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.favorites = favorites;
	}

	public static Member of(String email, String name, String password) {
		return new Member(null, email, name, password, new HashSet<>());
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
		if (this.favorites.contains(favorite)) {
			throw new DuplicateFavoriteException("이미 존재하는 즐겨찾기 입니다.");
		}
		Set<Favorite> newFavorites = new HashSet<>(this.favorites);
		newFavorites.add(favorite);
		return new Member(this.id, this.email, this.name, this.password, newFavorites);
	}

	public Member deleteFavorite(final Favorite favorite) {
		Set<Favorite> newFavorites = this.favorites.stream()
			.filter(it -> !it.equals(favorite))
			.collect(toSet());
		return new Member(this.id, this.email, this.name, this.password, newFavorites);
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
		return favorites;
	}
}
