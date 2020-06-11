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
		Set<Favorite> addedFavorites = new HashSet<>(favorites);
		addedFavorites.add(favorite);

		if (isSizeEqual(addedFavorites)) {
			throw new IllegalArgumentException("중복된 요소를 추가할 수 없습니다.");
		}

		return new Favorites(addedFavorites);
	}

	public Favorites remove(long sourceId, long targetId) {
		Set<Favorite> removedFavorites = favorites.stream()
			.filter(fav -> !fav.equalsSourceAndTarget(sourceId, targetId))
			.collect(Collectors.toCollection(HashSet::new));

		if (isSizeEqual(removedFavorites)) {
			throw new IllegalArgumentException("제거할 요소를 찾을 수 없습니다.");
		}

		return new Favorites(removedFavorites);
	}

	private boolean isSizeEqual(Set<Favorite> other) {
		return favorites.size() == other.size();
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
