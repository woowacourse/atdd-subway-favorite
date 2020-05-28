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
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void save(Member member, FavoriteStation favoriteStation) {
        if (member.contain(favoriteStation)) {
            throw new IllegalArgumentException();
        }
        member.addFavoriteStation(favoriteStation);
        memberRepository.save(member);
    }

    public FavoritesResponse findAll(Member member) {
        List<FavoriteResponse> favoriteStations = new ArrayList<>();

        for (FavoriteStation favoriteStation : member.getFavoriteStations()) {
            Station source = stationRepository.findById(favoriteStation.getSource())
                    .orElseThrow(NoSuchElementException::new);
            Station target = stationRepository.findById(favoriteStation.getTarget())
                    .orElseThrow(NoSuchElementException::new);
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