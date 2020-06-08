package wooteco.subway.service.favorite;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteDetail;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public void addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        if (favoriteRepository.hasFavorite(memberId, favoriteRequest.getSourceStationId(),
            favoriteRequest.getTargetStationId())) {
            throw new IllegalArgumentException("동일한 즐겨찾기가 등록되어 있습니다.");
        }
        favoriteRepository.save(favoriteRequest.toFavorite(memberId));
    }

    public List<FavoriteDetail> getFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        Set<Long> favoriteStationIds = findAllIds(favorites);

        Map<Long, Station> stations = stationRepository.findAllById(favoriteStationIds).stream()
            .collect(toMap(Station::getId, station -> station));

        return favorites.stream()
            .map(favorite -> toFavoriteDetail(favorite, stations))
            .collect(Collectors.toList());
    }

    private FavoriteDetail toFavoriteDetail(Favorite favorite, Map<Long, Station> stations) {
        return new FavoriteDetail(
            favorite.getId(),
            stations.get(favorite.getSourceStationId()).getId(),
            stations.get(favorite.getTargetStationId()).getId(),
            stations.get(favorite.getSourceStationId()).getName(),
            stations.get(favorite.getTargetStationId()).getName());
    }

    private Set<Long> findAllIds(List<Favorite> favorites) {
        Set<Long> ids = new HashSet<>();
        for (Favorite favorite : favorites) {
            ids.add(favorite.getSourceStationId());
            ids.add(favorite.getTargetStationId());
        }
        return Collections.unmodifiableSet(ids);
    }

    public boolean hasFavorite(Long memberId, Long sourceId, Long targetId) {
        return favoriteRepository.hasFavorite(memberId, sourceId, targetId);
    }

    public void removeFavoriteById(Long memberId, Long favoriteId) {
        favoriteRepository.deleteByIdWithMemberId(memberId, favoriteId);
    }

}
