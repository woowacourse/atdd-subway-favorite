package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    public static final String TEST_USER_EMAIL1 = "jason@email.com";
    public static final String TEST_USER_NAME1 = "제이슨";
    public static final String TEST_USER_PASSWORD1 = "jason";

    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository);
    }

    @Test
    void deleteFavoriteByStationId() {
        List<Favorite> favorites = Arrays.asList(new Favorite(1L, 2L),
                new Favorite(2L, 3L), new Favorite(3L, 4L));
        List<Favorite> favorites1 = Arrays.asList(new Favorite(1L, 5L),
                new Favorite(2L, 4L), new Favorite(6L, 7L));
        List<Member> members = Arrays.asList(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD, new HashSet<>(favorites)),
                new Member(TEST_USER_EMAIL1, TEST_USER_NAME1, TEST_USER_PASSWORD1, new HashSet<>(favorites1)));
        when(memberRepository.findAll()).thenReturn(members);

        favoriteService.deleteFavoriteByStationId(1L);

        verify(memberRepository).saveAll(any());
    }
}