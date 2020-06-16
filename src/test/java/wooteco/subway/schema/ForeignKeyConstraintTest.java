package wooteco.subway.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.DummyTestUserInfo.*;

@SpringBootTest
public class ForeignKeyConstraintTest {
    @Autowired
    StationRepository stationRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    private void deleteAll() {
        stationRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("시작역이 사라지면 연관된 즐겨찾기가 사라진다.")
    void onSourceStationDeleteTest() {
        Station station1 = new Station("1번역");
        Station station2 = new Station("2번역");
        stationRepository.save(station1);
        stationRepository.save(station2);

        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(station1.getId(), station2.getId()));
        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getFavorites().size()).isEqualTo(1);

        stationRepository.delete(station1);
        savedMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(savedMember.getFavorites().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("목적지역이 사라지면 연관된 즐겨찾기가 사라진다.")
    void onTargetStationDeleteTest() {
        Station station1 = new Station("1번역");
        Station station2 = new Station("2번역");
        stationRepository.save(station1);
        stationRepository.save(station2);

        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(station1.getId(), station2.getId()));
        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getFavorites().size()).isEqualTo(1);

        stationRepository.delete(station2);
        savedMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(savedMember.getFavorites().size()).isEqualTo(0);
    }
}
