package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.List;

@Service
public class FavoritesService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoritesService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void addFavorite(Member member, FavoriteCreateRequest favoriteCreateRequest) {
        member.addFavorite(favoriteCreateRequest.toEntity());
        memberRepository.save(member);
    }

    public List<FavoriteResponse> getFavorites(Member member) {
        List<Long> favoriteStationIds = member.getFavoriteStationIds();
        Stations stations = new Stations(stationRepository.findAllById(favoriteStationIds));

        return FavoriteResponse.listOf(member.getFavorites(), stations.createStationIdNameMap());
    }

    public void deleteFavorite(Member member, Long favoriteId) {
        Favorite targetFavorite = member.findFavoriteById(favoriteId);
        member.deleteFavorite(targetFavorite);
        memberRepository.save(member);
    }
}
