package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;
import wooteco.subway.service.line.LineStationService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.station.StationService;

@DataJdbcTest
@Import(value = {FavoriteService.class, MemberService.class, JwtTokenProvider.class, StationService.class, LineStationService.class})
@Sql("/truncate.sql")
class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StationService stationService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private FavoriteRequest favoriteRequest;
    private Long memberId;

    @BeforeEach
    void setUp() {
        stationService.createStation(new Station(null, "잠실"));
        stationService.createStation(new Station(null, "강남"));
        favoriteRequest = new FavoriteRequest("잠실", "강남");
        MemberResponse member = memberService.createMember(new MemberRequest("test@test.com", "test", "test"));
        memberId = member.getId();
    }

    @Test
    void createWhenValidInput() {
        favoriteService.create(memberId, favoriteRequest);

        assertThat(favoriteService.findAll(memberId)).isNotEmpty();
    }

    @Test
    void createWhenDuplicatedInput() {
        favoriteService.create(memberId, favoriteRequest);

        assertThatThrownBy(() -> favoriteService.create(memberId, favoriteRequest))
                .isInstanceOf(DuplicateFavoriteException.class);
    }

    @Test
    void deleteTest() {
        favoriteService.create(memberId, favoriteRequest);

        favoriteService.delete(memberId, favoriteRequest);
        assertThat(favoriteService.findAll(memberId)).isEmpty();
    }
}

