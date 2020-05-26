package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    static final String EMAIL = "chomily@woowahan.com";
    static final String NAME = "chomily";
    static final String PASSWORD = "chomily1234";
    static final Long ID = 1L;

    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository);
    }

    @DisplayName("즐겨찾기 추가 기능 테스트")
    @Test
    void createFavorite() {
        Station preStation = new Station(1L, "강남역");
        Station station = new Station(2L, "선릉역");

        Favorite favorite = new Favorite(preStation.getId(), station.getId());

        Member member = new Member(ID, EMAIL, NAME, PASSWORD);

        Member updatedMember = new Member(ID, EMAIL, NAME, PASSWORD);
        updatedMember.addFavorite(favorite);

        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        FavoriteRequest favoriteRequest = new FavoriteRequest(preStation.getId(), station.getId());

        Favorite savedFavorite = favoriteService.createFavorite(member, favoriteRequest);

        assertThat(savedFavorite.getPreStation()).isEqualTo(preStation.getId());
        assertThat(savedFavorite.getStation()).isEqualTo(station.getId());
    }
}
