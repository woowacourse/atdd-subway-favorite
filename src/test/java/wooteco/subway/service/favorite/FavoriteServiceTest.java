package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import wooteco.subway.domain.favorite.exception.DuplicatedFavoriteException;
import wooteco.subway.domain.member.Member;
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
        given(stationService.findIdsByNames(request1.toList())).willReturn(Arrays.asList(1L, 2L));
        given(stationService.findIdsByNames(request2.toList())).willReturn(Arrays.asList(1L, 3L));

        favoriteService.addToMember(member, request1);
        favoriteService.addToMember(member, request2);
        assertThat(member.getFavorites()).hasSize(2);
    }

    @Test
    void addDuplicatedFavoriteToMember() {
        given(memberService.save(any())).willReturn(member);
        given(stationService.findIdsByNames(request1.toList())).willReturn(Arrays.asList(1L, 2L));

        favoriteService.addToMember(member, request1);
        assertThatThrownBy(() -> favoriteService.addToMember(member, request1)).isInstanceOf(
            DuplicatedFavoriteException.class);
    }

    @Test
    void deleteById() {
        given(stationService.findIdsByNames(request1.toList())).willReturn(Arrays.asList(1L, 2L));
        given(memberService.save(any())).willReturn(member);

        favoriteService.addToMember(member, request1);
        assertThat(member.getFavorites()).hasSize(1);

        favoriteService.deleteById(member, null);
        assertThat(member.getFavorites()).hasSize(0);
    }
}
