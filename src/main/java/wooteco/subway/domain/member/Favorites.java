package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;

public class Favorites {
	private Set<Favorite> favorites;

	public Favorites(Set<Favorite> favorites) {
		this.favorites = favorites;
	}

	public static Favorites empty() {
		return new Favorites(new HashSet<>());
	}

	public void add(Favorite favorite) {
		favorites.add(favorite);
	}

	public void remove(Favorite favorite) {
		favorites.remove(favorite);
	}

	public Set<Favorite> getFavorites() {
		return favorites;
	}
}
