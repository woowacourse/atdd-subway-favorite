package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.DuplicatedFavoriteException;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.StationService;

@ExtendWith(SpringExtension.class)
public class FavoriteServiceTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private StationService stationService;

    private FavoriteService favoriteService;
    private Member member;
    private FavoriteRequest request1;
    private FavoriteRequest request2;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberService, stationService);
        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        request1 = new FavoriteRequest("잠실", "잠실새내");
        request2 = new FavoriteRequest("잠실", "삼전");
    }

    @Test
    void addToMember() {
        given(memberService.save(any())).willReturn(member);
        given(stationService.findStationIdByName("잠실")).willReturn(1L);
        given(stationService.findStationIdByName("잠실새내")).willReturn(2L);
        given(stationService.findStationIdByName("삼전")).willReturn(4L);

        favoriteService.addToMember(member, request1);
        favoriteService.addToMember(member, request2);
        assertThat(member.getFavorites()).hasSize(2);
    }

    @Test
    void addDuplicatedFavoriteToMember() {
        given(memberService.save(any())).willReturn(member);
        given(stationService.findStationIdByName("잠실")).willReturn(1L);
        given(stationService.findStationIdByName("잠실새내")).willReturn(2L);

        favoriteService.addToMember(member, request1);
        assertThatThrownBy(()-> favoriteService.addToMember(member, request1)).isInstanceOf(
            DuplicatedFavoriteException.class);
    }


    @Test
    void deleteById() {
        given(memberService.save(any())).willReturn(member);

        favoriteService.addToMember(member, request1);

        favoriteService.deleteById(member, null);
        assertThat(member.getFavorites()).hasSize(0);
    }
}
