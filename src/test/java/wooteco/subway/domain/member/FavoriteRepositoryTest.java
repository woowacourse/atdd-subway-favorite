package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@DataJpaTest
public class FavoriteRepositoryTest {
    @Autowired
    private FavoriteRepository favorites;

    @Autowired
    private MemberRepository members;

    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        Member member = members.findByEmail("test@test.com").get();
        Station station1 = stations.findByName("테스트역1").get();
        Station station3 = stations.findByName("테스트역3").get();

        Favorite favorite = favorites.save(new Favorite(member, station1, station3));

        assertThat(favorite.getId()).isNotNull();
    }

    @Test
    void saveWithoutId() {
        Member member = new Member("이메일", "이름", "비밀번호");
        Station station1 = new Station("역이름1");
        Station station3 = new Station("역이름2");

        assertThatThrownBy(() -> favorites.save(new Favorite(member, station1, station3)))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    void saveWithInvalidId() {
        Member member = new Member(9999L, "이메일", "이름", "비밀번호");
        Station station1 = new Station(9999L, "역이름1");
        Station station3 = new Station(9999L, "역이름2");

        assertThatThrownBy(() -> favorites.save(new Favorite(member, station1, station3)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByMemberId() {
        List<Favorite> favoriteList = favorites.findAllByMemberId(1);

        assertThat(favoriteList).hasSize(3);
    }

    @Test
    void findByMemberName() {
        List<Favorite> favoriteList = favorites.findAllByMemberName("testUser2");

        assertThat(favoriteList).hasSize(2);
    }

    @Test
    void findByAllInfo() {
        Favorite favorite = favorites.findByMemberIdAndSourceIdAndTargetId(1L, 1L, 2L).get();

        assertThat(favorite.getId()).isEqualTo(1L);
    }
}
