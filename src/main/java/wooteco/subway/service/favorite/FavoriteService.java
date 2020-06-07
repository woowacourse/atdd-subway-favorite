package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FavoriteService {
    private static final String STATION_NOT_EXIST_ERROR_MESSAGE = "해당 역이 존재하지 않습니다.";
    private static final String ALREADY_EXIST_FAVORITE_ERROR_MESSAGE = "이미 등록된 즐겨찾기입니다.";
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void save(Member member, FavoriteStation favoriteStation) {
        if (member.contain(favoriteStation)) {
            throw new IllegalArgumentException(ALREADY_EXIST_FAVORITE_ERROR_MESSAGE);
        }
        member.addFavoriteStation(favoriteStation);
        memberRepository.save(member);
    }

    public FavoritesResponse findAll(Member member) {
        List<FavoriteResponse> favoriteStations = new ArrayList<>();

        for (FavoriteStation favoriteStation : member.getFavoriteStations()) {
            List<Station> stations = stationRepository.findAll();
            Station source = stations.stream()
                    .filter(station -> station.getId().equals(favoriteStation.getSource()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(STATION_NOT_EXIST_ERROR_MESSAGE));
            Station target = stations.stream()
                    .filter(station -> station.getId().equals(favoriteStation.getTarget()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(STATION_NOT_EXIST_ERROR_MESSAGE));
            favoriteStations.add(
                    new FavoriteResponse(member.getId(), StationResponse.of(source), StationResponse.of(target)));
        }
        return FavoritesResponse.of(favoriteStations);
    }

    public void delete(Member member, Long source, Long target) {
        FavoriteStation favoriteStation = member.findByNames(source, target);
        member.deleteFavoriteStation(favoriteStation);
        memberRepository.save(member);
    }
}