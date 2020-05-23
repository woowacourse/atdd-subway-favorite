package wooteco.subway.domain.member;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.domain.favorite.Favorite;

public class Member {
	@MappedCollection(idColumn = "member", keyColumn = "id")
	List<Favorite> favorites = new ArrayList<>();
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

	@Override
	public String toString() {
		return "Member{" +
			"id=" + id +
			", email='" + email + '\'' +
			", name='" + name + '\'' +
			", password='" + password + '\'' +
			'}';
	}

	public List<Favorite> getFavorites() {
		return favorites;
	}

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}
}
