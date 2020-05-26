package wooteco.subway.domain.member;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	public Set<Long> extractStationIds() {
		return favorites.stream()
				.map(favorite -> Arrays.asList(favorite.getSourceId(), favorite.getTargetId()))
				.flatMap(List::stream)
				.collect(Collectors.toSet());
	}

	public Set<Favorite> getFavorites() {
		return favorites;
	}
}
