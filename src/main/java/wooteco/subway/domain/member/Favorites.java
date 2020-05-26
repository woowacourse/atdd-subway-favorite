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

	public void removeById(Long sourceId, Long targetId) {
		favorites.stream()
				.filter(favorite -> favorite.hasSourceId(sourceId) && favorite.hasTargetId(targetId))
				.findFirst()
				.ifPresent(this::remove);
	}

	public Set<Favorite> getFavorites() {
		return favorites;
	}
}
