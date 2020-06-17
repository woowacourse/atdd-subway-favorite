package wooteco.subway.service.favorite;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class FavoriteServiceTest {
    //
    // @MockBean
    // private MemberService memberService;
    //
    // @MockBean
    // private StationService stationService;
    //
    // @MockBean
    // private FavoriteRepository favoriteRepository;
    //
    // private FavoriteService favoriteService;
    // private Member member;
    // private FavoriteRequest request1;
    // private FavoriteRequest request2;
    // private Favorite favorite1;
    //
    // @BeforeEach
    // void setUp() {
    //     favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);
    //     member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    //     request1 = new FavoriteRequest("잠실", "잠실새내");
    //     request2 = new FavoriteRequest("잠실", "삼전");
    //     favorite1 = Favorite.of(member, 1L, 1L, 2L);
    // }
    //
    // @Test
    // void addToMember() {
    //     given(favoriteRepository.save(any())).willReturn(favorite1);
    //     given(stationService.findStationIdByName("잠실")).willReturn(1L);
    //     given(stationService.findStationIdByName("잠실새내")).willReturn(2L);
    //     given(stationService.findStationIdByName("삼전")).willReturn(4L);
    //
    //     FavoriteResponse response = favoriteService.create(member, request1);
    //     assertThat(response).isEqualToComparingOnlyGivenFields(favorite1, "id", "memberId",
    //         "sourceId", "targetId");
    // }
    //
    // @Test
    // void deleteById() {
    //     given(favoriteRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(
    //         Optional.of(favorite1));
    //     favoriteService.delete(member, favorite1.getId());
    // }
}
