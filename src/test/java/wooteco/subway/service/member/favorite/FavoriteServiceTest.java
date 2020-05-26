package wooteco.subway.service.member.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.service.member.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository);
        member = new Member(1L, TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
        member.addFavorite(new Favorite(1L, STATION_NAME_YEOKSAM, STATION_NAME_DOGOK));
        member.addFavorite(new Favorite(2L, STATION_NAME_HANTI, STATION_NAME_YANGJAE));
        member.addFavorite(new Favorite(3L, STATION_NAME_DOGOK, STATION_NAME_MAEBONG));
    }

    @Test
    void createFavorite() {
        FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG);
        member.addFavorite(new Favorite(4L, request.getSource(), request.getTarget()));
        when(memberRepository.save(any())).thenReturn(member);

        FavoriteResponse favorite = favoriteService.createFavorite(member, request);

        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getSource()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(favorite.getTarget()).isEqualTo(STATION_NAME_SEOLLEUNG);
    }

    @Test
    void findFavorites() {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(member);

        assertThat(favorites).hasSize(3);
    }

    @Test
    void deleteFavorite() {
        favoriteService.deleteFavorite(member, 1L);

        assertThat(favoriteService.findFavorites(member)).hasSize(2);
    }
}