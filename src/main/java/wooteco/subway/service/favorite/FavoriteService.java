package wooteco.subway.service.favorite;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteExistenceResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Transactional
@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(Member member) {
        Set<Long> favoriteStationIds = member.findAllFavoriteStationIds();
        Map<Long, Station> stations = stationRepository.findAllById(favoriteStationIds).stream()
            .collect(toMap(Station::getId, station -> station));

        return member.getFavorites().stream()
            .map(favorite -> makeFavoriteResponse(favorite, stations))
            .collect(Collectors.toList());
    }

    private FavoriteResponse makeFavoriteResponse(Favorite favorite, Map<Long, Station> stations) {
        return FavoriteResponse.of(
            stations.get(favorite.getSourceStationId()), stations.get(favorite.getTargetStationId()));
    }

    public FavoriteExistenceResponse hasFavoritePath(Member member, Long sourceStationId, Long targetStationId) {
        Favorite request = new Favorite(sourceStationId, targetStationId);
        return new FavoriteExistenceResponse(member.hasFavorite(request));
    }

    public void addFavorite(Member member, FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRequest.toFavorite();
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    public void removeFavorite(Member member, Long sourceId, Long targetId) {
        Favorite favorite = new Favorite(sourceId, targetId);
        member.removeFavorite(favorite);
        memberRepository.save(member);
    }
}
