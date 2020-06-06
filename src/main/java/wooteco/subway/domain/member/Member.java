package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteNotFoundException;

public class Member {
	Set<Favorite> favorites = new HashSet<>();
	@Id
	private Long id;
	private String email;
	private String name;
	private String password;

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

	public void update(String name, String password) {
		if (StringUtils.isNotBlank(name)) {
			this.name = name;
		}
		if (StringUtils.isNotBlank(password)) {
			this.password = password;
		}
	}

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	public void deleteFavorite(Long sourceStationId, Long targetStationId) {
		int preSize = favorites.size();
		Favorite favorite = new Favorite(sourceStationId, targetStationId);
		favorites.remove(favorite);

		if (preSize == favorites.size()) {
			throw new FavoriteNotFoundException();
		}
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
