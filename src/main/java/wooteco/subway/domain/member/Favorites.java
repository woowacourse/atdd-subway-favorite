package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.service.station.DuplicateFavoriteException;

/**
 *    즐겨찾기 일급 컬렉션 class입니다.
 *
 *    @author HyungJu An
 */
public class Favorites {
	private final Set<Favorite> favorites;

	Favorites(final Set<Favorite> favorites) {
		this.favorites = favorites;
	}

	public static Favorites of(final Set<Favorite> favorites) {
		return new Favorites(favorites);
	}

	public Favorites add(final Favorite favorite) {
		if (this.favorites.contains(favorite)) {
			throw new DuplicateFavoriteException("이미 존재하는 즐겨찾기 입니다.");
		}
		Set<Favorite> newFavorites = new HashSet<>(this.favorites);
		newFavorites.add(favorite);
		return new Favorites(newFavorites);
	}

	public Favorites delete(final Favorite favorite) {
		return this.favorites.stream()
			.filter(it -> !it.equals(favorite))
			.collect(Collectors.collectingAndThen(Collectors.toSet(), Favorites::new));
	}

	public Set<Favorite> getFavorites() {
		return favorites;
	}
}
