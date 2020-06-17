package wooteco.subway.service.member;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@Service
public class FavoritesService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoritesService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void addFavorite(Member member, FavoriteCreateRequest favoriteCreateRequest) {
        member.addFavorite(favoriteCreateRequest.toEntity());
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavoritesBy(Member member) {
        List<Long> favoriteStationIds = member.getFavoriteStationIds();
        Stations stations = new Stations(stationRepository.findAllById(favoriteStationIds));

        return FavoriteResponse.listOf(member.getFavorites(), stations.createStationIdNameMap());
    }

    @Transactional
    public void deleteFavorite(Member member, FavoriteDeleteRequest favoriteDeleteRequest) {
        member.deleteFavorite(favoriteDeleteRequest.toEntity());
        memberRepository.save(member);
    }
}
