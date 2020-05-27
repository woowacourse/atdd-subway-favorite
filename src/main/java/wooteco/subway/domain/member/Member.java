package wooteco.subway.domain.member;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.domain.favorite.Favorite;

public class Member {
	@Id
	private Long id;
	private String email;
	private String name;
	private String password;

	@MappedCollection(idColumn = "member")
	private Set<Favorite> favorites = new LinkedHashSet<>();

	public Member() {
	}

	public Member(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public Member(Long id, String email, String name, String password) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
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

	public void update(String name, String password) {
		if (StringUtils.isNotBlank(name)) {
			this.name = name;
		}
		if (StringUtils.isNotBlank(password)) {
			this.password = password;
		}
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}

	public void deleteFavorite(Favorite favorite) {
		favorites = favorites.stream()
			.filter(fav -> !fav.equals(favorite))
			.collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
