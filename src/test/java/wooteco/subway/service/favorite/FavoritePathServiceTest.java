package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.StationService;
import wooteco.subway.service.station.dto.FavoritePathResponse;

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

    @Test
    void findAllOf() {
        // given
        Station start = new Station(1L, "신정역");
        Station end = new Station(2L, "잠실역");
        Member member = new Member("test@test.com", "name", "password");

        List<FavoritePath> foundFavoritePaths = Collections.singletonList(
            new FavoritePath(start, end));
        when(memberService.findFavoritePathsOf(member)).thenReturn(foundFavoritePaths);

        when(stationService.findNameById(start.getId())).thenReturn(start.getName());
        when(stationService.findNameById(end.getId())).thenReturn(end.getName());

        // when
        List<FavoritePathResponse> favoritePathResponses = favoritePathService.findAllOf(member);

        // then
        for (FavoritePathResponse favoritePathResponse : favoritePathResponses) {
            assertThat(favoritePathResponse.getStartStationName()).isEqualTo(start.getName());
            assertThat(favoritePathResponse.getEndStationName()).isEqualTo(end.getName());
        }
    }
}
