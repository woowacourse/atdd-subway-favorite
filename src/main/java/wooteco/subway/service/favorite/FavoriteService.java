package wooteco.subway.service.favorite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.InvalidStationIdException;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public Favorite createFavorite(Member member, FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRequest.toFavorite();
        member.addFavorite(favorite);
        try {
            Member updatedMember = memberRepository.save(member);
            return updatedMember.findFavorite(favorite)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 추가되지 않았습니다."));
        } catch (DbActionExecutionException e) {
            String message = e.getCause().getMessage();
            if (message.contains("FOREIGN KEY")) {
                throw new InvalidStationIdException("해당 역이 존재하지 않습니다.");
            }
            throw e;
        }
    }

    public List<FavoriteResponse> getFavorites(Member member) {
        List<Long> stationIds = member.getStationIdsFromFavorites();
        List<Station> stations = stationRepository.findAllById(stationIds);
        List<Favorite> favorites = member.getFavorites();

        return favorites.stream()
            .map(it -> FavoriteResponse.of(it.getId(), mapStationById(stations, it.getPreStation()),
                mapStationById(stations, it.getStation())))
            .collect(Collectors.toList());
    }

    private Station mapStationById(List<Station> stations, Long stationId) {
        return stations.stream()
            .filter(station -> station.getId().equals(stationId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("해당하는 역이 존재하지 않습니다."));
    }

    public void deleteFavorite(Member member, Long favoriteId) {
        member.removeFavoriteById(favoriteId);
        memberRepository.save(member);
    }
}
