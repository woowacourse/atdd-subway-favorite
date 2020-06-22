package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;

    @Test
    void addFavoritePath() {
        String email = "test@test.com";
        Member member = new Member(email, "testName", "testPassword");
        member = memberRepository.save(member);

        Station start = stationRepository.save(new Station("천호역"));
        Station end = stationRepository.save(new Station("강동구청역"));

        member.addFavoritePath(new FavoritePath(start, end));
        Member updated = memberRepository.save(member);
        assertThat(updated).isEqualTo(member);

        Member retrieved = memberRepository.findByEmail(updated.getEmail()).orElseThrow(IllegalArgumentException::new);
        assertThat(retrieved).isEqualTo(updated);
    }

    @Test
    void findByEmail() {
        String email = "member@member.com";
        Member expected = new Member(email, "testName", "testPassword");
        expected = memberRepository.save(expected);

        Station start = stationRepository.save(new Station("천호역"));
        Station end = stationRepository.save(new Station("강동구청역"));

        expected.addFavoritePath(new FavoritePath(start, end));
        expected = memberRepository.save(expected);

        Member member = memberRepository.findByEmail(expected.getEmail())
            .orElseThrow(IllegalAccessError::new);
        assertThat(member).isEqualTo(expected);
    }
}