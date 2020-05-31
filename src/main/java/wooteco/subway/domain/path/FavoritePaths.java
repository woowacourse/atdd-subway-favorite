package wooteco.subway.domain.path;

import org.springframework.data.relational.core.mapping.MappedCollection;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FavoritePaths {
	@MappedCollection(idColumn = "member", keyColumn = "member_key")
	private List<FavoritePath> favoritePaths;

	public FavoritePaths(final List<FavoritePath> favoritePaths) {
		this.favoritePaths = favoritePaths;
	}

	public static FavoritePaths empty() {
		return new FavoritePaths(new ArrayList<>());
	}

	public void addPath(FavoritePath favoritePath) {
		validateDuplication(favoritePath);

		if (Objects.nonNull(favoritePath)) {
			this.favoritePaths.add(favoritePath);
		}
	}

	private void validateDuplication(FavoritePath favoritePath) {
		favoritePaths.stream()
				.filter(path -> Objects.equals(path.getSourceId(), favoritePath.getSourceId()) &&
						Objects.equals(path.getTargetId(), favoritePath.getTargetId()))
				.findFirst()
				.ifPresent(path -> {
					throw new DuplicatedFavoritePathException();
				});
	}

	public boolean hasNotPath(Long pathId) {
		return favoritePaths.stream()
				.noneMatch(path -> Objects.equals(path.getId(), pathId));
	}

	public void deletePath(Long pathId) {
		favoritePaths = favoritePaths.stream()
				.filter(path -> !Objects.equals(path.getId(), pathId))
				.collect(Collectors.toList());
	}

	public FavoritePath getRecentlyUpdatedPath() {
		return favoritePaths.get(favoritePaths.size() - 1);
	}

	public List<Long> getPathsIds() {
		return favoritePaths.stream()
				.map(FavoritePath::getId)
				.collect(Collectors.toList());
	}

	public List<Long> getStationsIds() {
		return favoritePaths.stream()
				.map(FavoritePath::getStationsId)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
}
