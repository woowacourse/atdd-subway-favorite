package wooteco.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.member.favorite.Favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;


    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));

        assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station(stationName)));
    }

    @DisplayName("station <-> favorite 외래키 전략은 on delete cascade")
    @Test
    void stationDeleteFavorite() {
        //given
        Station station1 = stationRepository.save(new Station("강남"));
        Station station2 = stationRepository.save(new Station("강남sdf"));

        //when
        Member member = new Member("123", "123", "123");
        member.addFavorite(new Favorite(station1.getId(), station2.getId()));
        memberRepository.save(member);
        stationRepository.deleteAll();

        //then
        Member member1 = memberRepository.findByEmail("123").orElseThrow(RuntimeException::new);
        assertThat(member1.getFavorites().getFavorites()).isEmpty();
    }
}
