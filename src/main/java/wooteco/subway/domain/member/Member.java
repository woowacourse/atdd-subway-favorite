package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import wooteco.subway.domain.favorite.Favorite;

public class Member {
	@Id
	private final Long id;
	private final String email;
	private final String name;
	private final String password;

	private final Set<Favorite> favorites;

	Member(Long id, String email, String name, String password, Set<Favorite> favorites) {
		if (StringUtils.isBlank(email)) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException();
		}

		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.favorites = favorites;
	}

	public static Member of(String email, String name, String password) {
		return new Member(null, email, name, password, new HashSet<>());
	}

	public Member withId(Long id) {
		return new Member(id, this.email, this.name, this.password, this.favorites);
	}

	public Member withFavorites(Set<Favorite> favorites) {
		return new Member(this.id, this.email, this.name, this.password, this.favorites);
	}

	public Member updateNameAndPassword(String name, String password) {
		return new Member(this.id, this.email, name, password, this.favorites);
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}

	public void removeFavorite(Favorite favorite) {
		Set<Favorite> updated = favorites.stream()
			.filter(fav -> !fav.equalsExceptId(favorite))
			.collect(Collectors.toCollection(HashSet::new));
		favorites.clear();
		favorites.addAll(updated);
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
