package wooteco.subway.service.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;

@ExtendWith(MockitoExtension.class)
public class FavoritesServiceTest {
    private static final Member MEMBER_BROWN = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

    private FavoritesService favoritesService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        favoritesService = new FavoritesService(memberRepository);
    }

    @Test
    void addFavorite() {
        favoritesService.addFavorite(MEMBER_BROWN, new FavoriteCreateRequest(1L, 2L));

        verify(memberRepository).save(any());
    }
}
