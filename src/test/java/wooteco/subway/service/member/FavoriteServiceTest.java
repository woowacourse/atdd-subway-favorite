package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@SpringBootTest
@Sql("/truncate.sql")
class FavoriteServiceTest {

    private static final String TEST_EMAIL = "email@email.com";
    private static final String TEST_NAME = "한글";
    private static final String TEST_PASSWORD = "password";
    private static final String STATION_NAME_KANGNAM = "강남역";
    private static final String STATION_NAME_YEOKSAM = "역삼역";
    private static final String STATION_NAME_SEOLLEUNG = "선릉역";

    private FavoriteService favoriteService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    private Station station1;
    private Station station2;
    private Station station3;
    private Member member;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository);

        station1 = stationRepository.save(new Station(STATION_NAME_KANGNAM));
        station2 = stationRepository.save(new Station(STATION_NAME_YEOKSAM));
        station3 = stationRepository.save(new Station(STATION_NAME_SEOLLEUNG));

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
    void createFavorite() {
        FavoriteRequest request = new FavoriteRequest(station2.getId(), station3.getId());
        favoriteService.createFavorite(member, request);
        assertThat(member.getFavorites()).contains(request.toFavorite());
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