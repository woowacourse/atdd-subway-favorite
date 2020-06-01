package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoritePath registerPath(Member member, String sourceName, String targetName) {
        Station source = stationRepository.findByName(sourceName)
            .orElseThrow(() -> new NotExistStationException(sourceName));
        Station target = stationRepository.findByName(targetName)
            .orElseThrow(() -> new NotExistStationException(targetName));

        FavoritePath favoritePath = FavoritePath.of(source.getId(), target.getId());
        member.addFavoritePath(favoritePath);
        Member savedMember = memberRepository.save(member);
        return savedMember.getRecentlyUpdatedPath();
    }

    public List<FavoritePathResponse> retrievePath(Member member) {
        List<FavoritePath> favoritePaths = member.getFavoritePaths();
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
        if (member.hasNotPath(pathId)) {
            throw new NotExistFavoritePathException(pathId);
        }
        member.deletePath(pathId);
        memberRepository.save(member);
    }
}
