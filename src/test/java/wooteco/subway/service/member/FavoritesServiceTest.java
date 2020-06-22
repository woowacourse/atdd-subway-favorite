package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.AcceptanceTest.TEST_USER_NAME;
import static wooteco.subway.AcceptanceTest.TEST_USER_PASSWORD;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@Sql("/truncate.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(FavoritesService.class)
@DataJdbcTest
public class FavoritesServiceTest {
    private Member MEMBER_BROWN;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoritesService favoritesService;

    @BeforeEach
    void setUp() {
        MEMBER_BROWN = memberRepository.save(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        stationRepository.saveAll(Arrays.asList(
                new Station("서울"),
                new Station("강남"),
                new Station("잠실")
        ));
        MEMBER_BROWN.addFavorite(new Favorite(1L, 2L));
        MEMBER_BROWN.addFavorite(new Favorite(2L, 3L));
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        favoritesService.addFavorite(MEMBER_BROWN, new FavoriteCreateRequest(3L, 4L));

        List<FavoriteResponse> favorites = favoritesService.getFavoritesBy(MEMBER_BROWN);

        assertThat(favorites).hasSize(3);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {
        List<FavoriteResponse> favorites = favoritesService.getFavoritesBy(MEMBER_BROWN);

        assertThat(favorites.size()).isEqualTo(2);
    }

    @DisplayName("즐겨찾기 제거")
    @Test
    void deleteFavorite() {
        favoritesService.deleteFavorite(MEMBER_BROWN, new FavoriteDeleteRequest(1L, 2L));

        List<FavoriteResponse> favorites = favoritesService.getFavoritesBy(MEMBER_BROWN);

        assertThat(favorites.size()).isEqualTo(1);
    }
}
