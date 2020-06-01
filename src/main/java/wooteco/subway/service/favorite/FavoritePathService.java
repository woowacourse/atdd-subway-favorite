package wooteco.subway.service.favorite;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.path.FavoritePathDto;
import wooteco.subway.domain.path.FavoritePathRepository;
import wooteco.subway.domain.path.FavoritePaths;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;

@Service
public class FavoritePathService {
    private final StationRepository stationRepository;
    private final FavoritePathRepository favoritePathRepository;

    public FavoritePathService(StationRepository stationRepository, FavoritePathRepository favoritePathRepository) {
        this.stationRepository = stationRepository;
        this.favoritePathRepository = favoritePathRepository;
    }

    public FavoritePath registerPath(Member member, String sourceName, String targetName) {
        Station source = stationRepository.findByName(sourceName)
            .orElseThrow(() -> new NotExistStationException(sourceName));
        Station target = stationRepository.findByName(targetName)
            .orElseThrow(() -> new NotExistStationException(targetName));

        if (favoritePathRepository.findByUniqueField(member.getId(), source.getId(), target.getId()).isPresent()) {
            throw new DuplicatedFavoritePathException();
        }

        FavoritePath favoritePath = new FavoritePath(source.getId(), target.getId(), member.getId());
        return favoritePathRepository.save(favoritePath);
    }

    public List<FavoritePathResponse> retrievePath(Member member) {
        FavoritePaths favoritePaths = new FavoritePaths(favoritePathRepository.findAllByMemberId(member.getId()));

        Set<Long> stationIdsInFavoritePaths = favoritePaths.getAllStationIds();

        Stations stations = new Stations(stationRepository.findAllById(stationIdsInFavoritePaths));
        Map<Long, Station> stationsCache = stations.convertToMap();

        List<FavoritePathDto> favoritePathDtos = favoritePaths.getFavoritePathDtos(stationsCache);
        return FavoritePathResponse.listOf(favoritePathDtos);
    }

    public void deletePath(Member member, long pathId) {
        FavoritePath favoritePath = favoritePathRepository.findByMemberIdAndPathId(member.getId(), pathId)
            .orElseThrow(() -> new NotExistFavoritePathException(pathId));

        favoritePathRepository.delete(favoritePath);
    }
}
