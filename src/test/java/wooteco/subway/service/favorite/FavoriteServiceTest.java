package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private MemberRepository memberRepository;

    private FavoriteService favoriteService;
    private Member member;

    @BeforeEach
    public void setUp() {
        this.favoriteService = new FavoriteService(memberRepository);
        this.member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void addFavorite() {
        FavoriteCreateRequest request = new FavoriteCreateRequest(1L, 2L);
        when(memberRepository.save(member)).thenReturn(member);

        FavoriteResponse response = favoriteService.addFavorite(member, request);
        assertThat(response.getDepartureId()).isEqualTo(request.getDepartureId());
        assertThat(response.getDestinationId()).isEqualTo(request.getDestinationId());
    }

    @Test
    void findFavorites() {
        member.addFavorite(1L, 2L);
        member.addFavorite(3L, 4L);
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        FavoriteListResponse response = favoriteService.findFavorites(member);

        assertThat(response.getFavoriteResponses().size()).isEqualTo(2);
    }

}