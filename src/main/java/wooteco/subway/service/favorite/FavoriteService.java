package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository,
                           StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void create(Member member, FavoriteCreateRequest favoriteCreateRequest) {
        List<Station> stations = stationRepository.findAll();
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        Station sourceStation = findStationByName(favoriteCreateRequest.getSource(), stations);
        Station targetStation = findStationByName(favoriteCreateRequest.getTarget(), stations);
        Favorite favorite = new Favorite(sourceStation.getId(), targetStation.getId());

        persistMember.addFavorite(favorite);

        memberRepository.save(persistMember);
    }

    private Station findStationByName(String name, List<Station> stations) {
        return stations.stream()
                .filter(station -> name.equals(station.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> find(Member member) {
        List<Station> stations = stationRepository.findAll();
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        Set<Favorite> favorites = persistMember.getFavorites();

        return favorites.stream()
                .map(favorite -> toFavoriteResponse(favorite, stations))
                .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(Favorite favorite, List<Station> stations) {
        Station sourceStation = findStationById(favorite.getSource(), stations);
        Station targetStation = findStationById(favorite.getTarget(), stations);

        return new FavoriteResponse(sourceStation.getName(), targetStation.getName());
    }

    private Station findStationById(Long id, List<Station> stations) {
        return stations.stream()
                .filter(station -> id.equals(station.getId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다."));
    }

    @Transactional
    public void delete(Member member, FavoriteDeleteRequest favoriteDeleteRequest) {
        List<Station> stations = stationRepository.findAll();
        Favorite favorite = toFavorite(favoriteDeleteRequest, stations);
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        persistMember.deleteFavorite(favorite);

        memberRepository.save(persistMember);
    }

    private Favorite toFavorite(FavoriteDeleteRequest favoriteDeleteRequest, List<Station> stations) {
        Station sourceStation = findStationByName(favoriteDeleteRequest.getSource(), stations);
        Station targetStation = findStationByName(favoriteDeleteRequest.getTarget(), stations);

        return new Favorite(sourceStation.getId(), targetStation.getId());
    }
}
