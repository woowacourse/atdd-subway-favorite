package wooteco.subway.domain.favorite;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class Favorites {
	private final Set<Favorite> favorites;

	Favorites(Set<Favorite> favorites) {
		this.favorites = favorites;
	}

	public static Favorites of(Set<Favorite> favorites) {
		return new Favorites(new HashSet<>(favorites));
	}

	public static Favorites of(Favorite... favorites) {
		return new Favorites(Sets.newHashSet(favorites));
	}

	public static Favorites empty() {
		return new Favorites(new HashSet<>());
	}

	public Favorites add(Favorite favorite) {
		Set<Favorite> newFavorites = new HashSet<>(favorites);
		newFavorites.add(favorite);
		return new Favorites(newFavorites);
	}

	public Favorites removeFavorite(long sourceId, long targetId) {
		return new Favorites(favorites.stream()
			.filter(fav -> !fav.equalsSourceAndTarget(sourceId, targetId))
			.collect(Collectors.toCollection(HashSet::new)));
	}

	public Set<Long> getAllStationIds() {
		Set<Long> stationIds = new HashSet<>();
		for (Favorite favorite : favorites) {
			stationIds.add(favorite.getSourceId());
			stationIds.add(favorite.getTargetId());
		}
		return stationIds;
	}

	public Set<Favorite> getFavorites() {
		return new HashSet<>(favorites);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorites favorites1 = (Favorites)o;
		return Objects.equals(favorites, favorites1.favorites);
	}

	@Override
	public int hashCode() {
		return Objects.hash(favorites);
	}

	@Override
	public String toString() {
		return "Favorites{" +
			"favorites=" + favorites +
			'}';
	}
}
