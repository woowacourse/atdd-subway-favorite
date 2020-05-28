package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.member.Favorite;
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
    private static final String STATION_NAME_HANTI = "한티역";
    private static final String STATION_NAME_DOGOK = "도곡역";
    private static final String STATION_NAME_MAEBONG = "매봉역";
    private static final String STATION_NAME_YANGJAE = "양재역";

    private FavoriteService favoriteService;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository);
        member = new Member(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
        member.addFavorite(new Favorite(STATION_NAME_YEOKSAM, STATION_NAME_DOGOK));
        member.addFavorite(new Favorite(STATION_NAME_HANTI, STATION_NAME_YANGJAE));
        member.addFavorite(new Favorite(STATION_NAME_DOGOK, STATION_NAME_MAEBONG));
        member = memberRepository.save(member);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    void createFavorite() {
        FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG);
        favoriteService.createFavorite(member, request);
        assertThat(member.getFavorites()).contains(request.toFavorite());
    }

    @Test
    void findFavorites() {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(member);
        assertThat(favorites).hasSize(3);
    }

    @Test
    void deleteFavorite() {

        FavoriteRequest request = new FavoriteRequest(STATION_NAME_YEOKSAM, STATION_NAME_DOGOK);
        favoriteService.deleteFavorite(member, request);
        assertThat(favoriteService.findFavorites(member)).hasSize(2);
    }
}