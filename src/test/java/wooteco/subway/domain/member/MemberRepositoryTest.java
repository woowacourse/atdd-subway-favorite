package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@DataJdbcTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Test
    void addFavoritePath() {
        String email = "test@test.com";
        Member member = new Member(email, "testName", "testPassword");
        member = memberRepository.save(member);
        member = memberRepository.findByEmail(email)
            .orElseThrow(IllegalStateException::new);

        Station start = stationRepository.save(new Station("천호역"));
        Station end = stationRepository.save(new Station("강동구청역"));

//        String name, LocalTime startTime, java.time.LocalTime endTime, int intervalTime

//        Line line = new Line("8호선", LocalTime.now(), LocalTime.now(), 10);
//        line.addLineStation(new LineStation(start.getId(), end.getId(), 10, 10));
//        line = lineRepository.save(line);
        member.addFavoritePath(new FavoritePath(start, end));
        Member updated = memberRepository.save(member);
        assertThat(updated).isEqualTo(member);
    }
}