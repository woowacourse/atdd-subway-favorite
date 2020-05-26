package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @Test
    void removeFavoriteByStationId() {
        List<Favorite> favorites = Arrays.asList(new Favorite(1L, 2L), new Favorite(2L, 3L), new Favorite(3L, 4L));
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD, new HashSet<>(favorites));

        member.removeFavoriteByStationId(1L);
        assertThat(member.getFavorites().size()).isEqualTo(2);
    }
}