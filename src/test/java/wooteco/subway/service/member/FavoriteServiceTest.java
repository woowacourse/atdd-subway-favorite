package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    private static final String TEST_USER_EMAIL = "brown@email.com";
    private static final String TEST_USER_NAME = "브라운";
    private static final String TEST_USER_PASSWORD = "brown";

    private FavoriteService favoriteService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(stationRepository, memberRepository);
    }

    @DisplayName("즐겨찾기 생성 테스트")
    @Test
    void addFavorite() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Station source = new Station(1L, "잠실역");
        Station target = new Station(3L, "석촌역");
        given(stationRepository.findAllById(Arrays.asList(1L, 3L)))
                .willReturn(Arrays.asList(source, target));
        given(memberRepository.save(any())).willReturn(member);

        // when
        favoriteService.addFavorite(member, new FavoriteRequest(source.getId(), target.getId()));

        // then
        verify(memberRepository).save(any());
    }

    @DisplayName("즐겨찾기 목록 조회 테스트")
    @Test
    void getAllFavorites() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Station source = new Station(1L, "잠실역");
        Station source2 = new Station(2L, "삼성역");
        Station target = new Station(3L, "석촌역");
        Station target2 = new Station(4L, "선릉역");

        given(stationRepository.findAllById(Arrays.asList(1L, 3L)))
                .willReturn(Arrays.asList(source, target));
        given(stationRepository.findAllById(Arrays.asList(2L, 4L)))
                .willReturn(Arrays.asList(source2, target2));
        given(memberRepository.save(any())).willReturn(member);

        favoriteService.addFavorite(member, new FavoriteRequest(source.getId(), target.getId()));
        favoriteService.addFavorite(member, new FavoriteRequest(source2.getId(), target2.getId()));

        when(stationRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(source, target, source2, target2));

        // when
        List<FavoriteResponse> favorites = favoriteService.getAllFavorites(member);

        // then
        assertThat(favorites).hasSize(2);
    }

    @DisplayName("즐겨찾기 삭제 테스트")
    @Test
    void deleteFavorite() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        member.getFavorites().addFavorite(new Favorite(1L, 1L, 2L));

        // when
        favoriteService.removeFavoriteById(member, 1L);

        // then
        verify(memberRepository).save(any());
    }
}
