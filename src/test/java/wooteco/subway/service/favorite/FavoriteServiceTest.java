package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static wooteco.subway.service.member.MemberServiceTest.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;

    private FavoriteService favoriteService;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        favoriteService = new FavoriteService(memberRepository, stationRepository);
    }

    @Test
    void save() {
        favoriteService.save(member, new FavoriteStation(1L, 1L, 3L));
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("즐겨찾기 중복 예외 테스트")
    void duplicateFavorite() {
        member.addFavoriteStation(new FavoriteStation(1L, 1L, 3L));
        assertThatThrownBy(() -> favoriteService.save(member, new FavoriteStation(1L, 1L, 3L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}