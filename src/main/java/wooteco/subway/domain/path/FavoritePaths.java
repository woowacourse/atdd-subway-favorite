package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.domain.station.Station;

public class FavoritePaths {
    private List<FavoritePath> favoritePaths;

    public FavoritePaths(List<FavoritePath> favoritePaths) {
        this.favoritePaths = favoritePaths;
    }

    public Set<Long> getAllStationIds() {
        return favoritePaths.stream()
            .map(path -> Arrays.asList(path.getSourceId(), path.getTargetId()))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    public List<FavoritePathDto> getFavoritePathDtos(Map<Long, Station> stations) {
        return favoritePaths.stream()
            .map(path -> {
                Station source = stations.get(path.getSourceId());
                Station target = stations.get(path.getTargetId());
                return new FavoritePathDto(path.getId(), source, target);
            })
            .collect(Collectors.toList());
    }
}
