package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.StationService;

@Service
public class FavoritePathService {

    private MemberService memberService;
    private StationService stationService;

    public FavoritePathService(MemberService memberService, StationService stationService) {
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public void register(String startStationName, String endStationName, Member member) {
        Station startStation = stationService.findByName(startStationName);
        Station endStation = stationService.findByName(endStationName);

        FavoritePath favoritePath = new FavoritePath(startStation, endStation);
        member.addFavoritePath(favoritePath);

        memberService.updateMember(member);
    }

    public void delete(String startStationName, String endStationName, Member member) {
        Station startStation = stationService.findByName(startStationName);
        Station endStation = stationService.findByName(endStationName);

        memberService.removeFavoritePath(startStation, endStation, member);
    }
}
