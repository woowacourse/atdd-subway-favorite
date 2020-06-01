package wooteco.subway.service.favorite;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.path.FavoritePathRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.station.dto.StationResponse;

@Service
public class FavoritePathService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoritePathRepository favoritePathRepository;

    public FavoritePathService(MemberRepository memberRepository, StationRepository stationRepository,
        FavoritePathRepository favoritePathRepository) {
        this.memberRepository = memberRepository;
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
        List<FavoritePath> favoritePaths = favoritePathRepository.findAllByMemberId(member.getId());

        Set<Long> stationIdsInFavoritePaths = favoritePaths.stream()
            .map(path -> Arrays.asList(path.getSourceId(), path.getTargetId()))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Stations stations = new Stations(stationRepository.findAllById(stationIdsInFavoritePaths));
        Map<Long, Station> stationsCache = stations.convertToMap();

        return favoritePaths.stream()
            .map(path -> {
                StationResponse source = StationResponse.of(stationsCache.get(path.getSourceId()));
                StationResponse target = StationResponse.of(stationsCache.get(path.getTargetId()));
                return new FavoritePathResponse(path.getId(), source, target);
            })
            .collect(Collectors.toList());
    }

    public void deletePath(Member member, long pathId) {
        FavoritePath favoritePath = favoritePathRepository.findByMemberIdAndPathId(member.getId(), pathId)
            .orElseThrow(() -> new NotExistFavoritePathException(pathId));

        favoritePathRepository.delete(favoritePath);
    }
}
