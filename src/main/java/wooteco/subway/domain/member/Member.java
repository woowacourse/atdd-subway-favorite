package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.service.station.DuplicateFavoriteException;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Member {
	private static final Member EMPTY = new Member(null, "", "", "", new LinkedHashSet<>());

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String name;
	private String password;
	@OneToMany(mappedBy = "member", orphanRemoval = true)
	private Set<Favorite> favorites = new LinkedHashSet<>();

	protected Member() {
	}

	private Member(Long id, String email, String name, String password, Set<Favorite> favorites) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.favorites = favorites;
	}

	public static Member of(String email, String name, String password) {
		if (email.isEmpty() && name.isEmpty() && password.isEmpty()) {
			return EMPTY;
		}
		return new Member(null, email, name, password, new LinkedHashSet<>());
	}

	public static Member of(Long id, String email, String name, String password) {
		return new Member(id, email, name, password, new LinkedHashSet<>());
	}

	public void update(String name, String password) {
		if (StringUtils.isNotBlank(name)) {
			this.name = name;
		}
		if (StringUtils.isNotBlank(password)) {
			this.password = password;
		}
	}

	public void addFavorite(Favorite favorite) {
		if (favorites.contains(favorite)) {
			throw new DuplicateFavoriteException("이미 존재하는 즐겨찾기 입니다.");
		}
		favorites.add(favorite);
		favorite.setMember(this);
	}

	public void deleteFavoriteById(Long id) {
		favorites.removeIf(it -> Objects.equals(it.getId(), id));
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
