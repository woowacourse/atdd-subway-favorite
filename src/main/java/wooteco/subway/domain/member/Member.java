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
	@Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
	private final Favorites favorites;

	private Member(Long id, String email, String name, String password, Favorites favorites) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.favorites = favorites;
	}

	public static Member of(String email, String name, String password) {
		return new Member(null, email, name, password, null);
	}

	public static Member of(String email, String name, String password, Favorites favorites) {
		return new Member(null, email, name, password, favorites);
	}

	public Member withId(Long id) {
		return new Member(id, this.email, this.name, this.password, this.favorites);
	}

	public Member makeMemberUpdateBy(String name, String password) {
		return new Member(this.id, this.email, findFirstNotBlank(name, this.name),
			findFirstNotBlank(password, this.password), this.favorites);
	}

	private String findFirstNotBlank(String priorityFirst, String prioritySecond) {
		if (StringUtils.isNotBlank(priorityFirst)) {
			return priorityFirst;
		}
		return prioritySecond;
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	public Set<Long> findAllFavoriteStationIds() {
		return favorites.findAllIds();
	}

	public boolean hasFavorite(Favorite favorite) {
		return favorites.hasFavorite(favorite);
	}

	public void addFavorite(Favorite favorite) {
		favorites.addFavorite(favorite);
	}

	public void removeFavorite(Favorite favorite) {
		favorites.removeFavorite(favorite);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Member member = (Member) o;
		return id.equals(member.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
