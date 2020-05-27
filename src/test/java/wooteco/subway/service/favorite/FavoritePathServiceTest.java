package wooteco.subway.service.favorite;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.StationService;

class FavoritePathServiceTest {

    private MemberService memberService = mock(MemberService.class);
    private StationService stationService = mock(StationService.class);
    private FavoritePathService favoritePathService =
        new FavoritePathService(memberService, stationService);

    @Test
    void delete() {
        // given
        String startStationName = "신정역";
        String endStationName = "목동역";
        Member mockMember = mock(Member.class);

        Station start = new Station(startStationName);
        Station end = new Station(endStationName);
        when(stationService.findByName(startStationName)).thenReturn(start);
        when(stationService.findByName(endStationName)).thenReturn(end);

        // when
        favoritePathService.delete(startStationName, endStationName, mockMember);

        // then
        verify(memberService).removeFavoritePath(start, end, mockMember);
    }
}
