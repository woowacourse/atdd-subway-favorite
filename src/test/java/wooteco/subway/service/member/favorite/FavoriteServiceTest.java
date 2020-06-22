package wooteco.subway.service.member.favorite;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;

    // @BeforeEach
    // void setUp() {
    //     this.favoriteService = new FavoriteService(memberRepository, stationRepository);
    // }

    // @DisplayName("즐겨찾기 추가 서비스")
    // @Test
    // void addFavorite() {
    //     Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    //     when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    //
    //     favoriteService.addFavorite(1L, 1L, 2L);
    //
    //     assertThat(member.getFavorites().getFavorites().size()).isEqualTo(1);
    //
    //     verify(memberRepository).save(any());
    // }

    // @DisplayName("즐겨찾기 제거 서비스")
    // @Test
    // void removeFavorite() {
    //     Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    //     when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    //
    //     favoriteService.addFavorite(1L, 1L, 2L);
    //     favoriteService.removeFavorite(1L, 1L, 2L);
    //
    //     assertThat(member.getFavorites().getFavorites().size()).isEqualTo(0);
    //
    //     verify(memberRepository, times(2)).save(any());
    // }
}
