package wooteco.subway.service.favorite;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public void save(Member member, FavoriteStation favoriteStation) {
        List<FavoriteStation> stations = favoriteRepository.findByMemberId(member.getId());
        if (isExistFavorite(favoriteStation, stations)) {
            throw new IllegalArgumentException();
        }
        favoriteRepository.save(favoriteStation);
    }

    private boolean isExistFavorite(FavoriteStation favoriteStation, List<FavoriteStation> stations) {
        return stations.stream()
            .anyMatch(station ->
                station.isSameSourceId(favoriteStation.getSourceId()) && station.isSameTargetId(favoriteStation.getTargetId())
            );
    }

    public FavoriteResponses findAll(Member member) {
        Map<Long, String> stationMap = getStationMap(getIdsByMemberId(member));

        List<FavoriteResponse> favoriteResponses = favoriteRepository.findByMemberId(member.getId()).stream()
            .map(favorite -> new FavoriteResponse(favorite,
                stationMap.get(favorite.getSourceId()),
                stationMap.get(favorite.getTargetId())))
            .collect(Collectors.toList());
        return FavoriteResponses.of(favoriteResponses);
    }

    private Map<Long, String> getStationMap(Set<Long> favoriteIds) {
        List<Station> stations = stationRepository.findAllById(favoriteIds);
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, Station::getName));
    }

    private Set<Long> getIdsByMemberId(Member member) {
        return favoriteRepository.findByMemberId(member.getId())
            .stream()
            .flatMap(favoriteStation -> Stream.of(favoriteStation.getSourceId(), favoriteStation.getTargetId()))
            .collect(Collectors.toSet());
    }

    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}