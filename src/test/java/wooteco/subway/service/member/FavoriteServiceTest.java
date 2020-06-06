package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.path.PathService;

@SpringBootTest
@Sql("/truncate.sql")
class FavoriteServiceTest {

    private static final String TEST_EMAIL = "email@email.com";
    private static final String TEST_NAME = "한글";
    private static final String TEST_PASSWORD = "password";
    private static final String STATION_NAME_KANGNAM = "강남역";
    private static final String STATION_NAME_YEOKSAM = "역삼역";
    private static final String STATION_NAME_SEOLLEUNG = "선릉역";
    private static final String STATION_NAME_JAMSIL = "잠실역";

    private FavoriteService favoriteService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private PathService pathService;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Line line;
    private Member member;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, pathService);

        station1 = stationRepository.save(new Station(STATION_NAME_KANGNAM));
        station2 = stationRepository.save(new Station(STATION_NAME_YEOKSAM));
        station3 = stationRepository.save(new Station(STATION_NAME_SEOLLEUNG));
        station4 = stationRepository.save(new Station(STATION_NAME_JAMSIL));

        line = new Line("1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, station1.getId(), 0, 0));
        line.addLineStation(new LineStation(station1.getId(), station2.getId(), 1, 1));
        line.addLineStation(new LineStation(station2.getId(), station3.getId(), 1, 1));
        line = lineRepository.save(line);

        member = new Member(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
        member.addFavorite(new Favorite(station1.getId(), station2.getId()));
        member.addFavorite(new Favorite(station1.getId(), station3.getId()));
        member.addFavorite(new Favorite(station3.getId(), station1.getId()));
        member = memberRepository.save(member);
    }

    @AfterEach
    void tearDown() {
        stationRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void createFavorite_Success() {
        FavoriteRequest request = new FavoriteRequest(station2.getId(), station3.getId());
        favoriteService.createFavorite(member, request);
        assertThat(member.getFavorites()).contains(request.toFavorite());
    }

    @Test
    void createFavorite_Fail_When_ImpossiblePath() {
        FavoriteRequest request = new FavoriteRequest(station1.getId(), station4.getId());
        assertThatThrownBy(() -> favoriteService.createFavorite(member, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createFavorite_Fail_When_Duplicate() {
        FavoriteRequest request = new FavoriteRequest(station1.getId(), station2.getId());
        assertThatThrownBy(() -> favoriteService.createFavorite(member, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findFavorites() {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(member);
        assertThat(favorites).hasSize(3);
    }

    @Test
    void deleteFavorite() {
        FavoriteDeleteRequest request = new FavoriteDeleteRequest(STATION_NAME_KANGNAM, STATION_NAME_YEOKSAM);
        favoriteService.deleteFavorite(member, request);
        assertThat(favoriteService.findFavorites(member)).hasSize(2);
    }
}