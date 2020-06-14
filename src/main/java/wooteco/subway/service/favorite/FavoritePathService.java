package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.favoritepath.RegisterFavoritePathException;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.StationService;
import wooteco.subway.service.station.dto.FavoritePathResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritePathService {

    private MemberService memberService;
    private StationService stationService;

    public FavoritePathService(MemberService memberService, StationService stationService) {
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void register(String startStationName, String endStationName, Member member) {
        Station startStation = stationService.findByName(startStationName);
        Station endStation = stationService.findByName(endStationName);
        FavoritePath favoritePath = new FavoritePath(startStation, endStation);
        if (member.has(favoritePath)) {
            throw new RegisterFavoritePathException(RegisterFavoritePathException.ALREADY_REGISTERED_MESSAGE);
        }

        memberService.addFavoritePath(member, favoritePath);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void delete(String startStationName, String endStationName, Member member) {
        Station startStation = stationService.findByName(startStationName);
        Station endStation = stationService.findByName(endStationName);

        memberService.removeFavoritePath(startStation, endStation, member);
    }

    @Transactional
    public List<FavoritePathResponse> findAllOf(Member member) {
        List<FavoritePathResponse> favoritePathResponses = new ArrayList<>();
        List<FavoritePath> favoritePaths = memberService.findFavoritePathsOf(member);

        for (FavoritePath favoritePath : favoritePaths) {
            String startStationName = favoritePath.getStartStationName();
            String endStationName = favoritePath.getEndStationName();
            FavoritePathResponse favoritePathResponse = new FavoritePathResponse(startStationName,
                endStationName);

            favoritePathResponses.add(favoritePathResponse);
        }
        return favoritePathResponses;
    }
}
