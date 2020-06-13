package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.DummyTestUserInfo.*;

@SpringBootTest
@Sql("/truncate.sql")
class FavoriteRepositoryTest {
    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void deleteAll() {
        stationRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("시작역으로 즐겨찾기 찾기")
    void findBySourceId() {
        Station station1 = new Station("1번역");
        Station station2 = new Station("2번역");
        Station savedStation1 = stationRepository.save(station1);
        Station savedStation2 = stationRepository.save(station2);

        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(savedStation1.getId(), savedStation2.getId()));
        memberRepository.save(member);

        Favorite favoriteBySourceId = favoriteRepository.findBySourceId(savedStation1.getId())
                .orElseThrow(() -> new NotExistFavoriteDataException("ID = " + savedStation1.getId()));

        assertThat(favoriteBySourceId.getId()).isNotNull();
        assertThat(favoriteBySourceId.getSourceId()).isEqualTo(savedStation1.getId());
        assertThat(favoriteBySourceId.getTargetId()).isEqualTo(savedStation2.getId());
    }

    @Test
    @DisplayName("도착역으로 즐겨찾기 찾기")
    void findByTargetId() {
        Station station1 = new Station("1번역");
        Station station2 = new Station("2번역");
        Station savedStation1 = stationRepository.save(station1);
        Station savedStation2 = stationRepository.save(station2);

        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(savedStation1.getId(), savedStation2.getId()));
        memberRepository.save(member);

        Favorite favoriteBySourceId = favoriteRepository.findByTargetId(savedStation2.getId())
                .orElseThrow(() -> new NotExistFavoriteDataException("ID = " + savedStation2.getId()));

        assertThat(favoriteBySourceId.getId()).isNotNull();
        assertThat(favoriteBySourceId.getSourceId()).isEqualTo(savedStation1.getId());
        assertThat(favoriteBySourceId.getTargetId()).isEqualTo(savedStation2.getId());
    }

    @Test
    @DisplayName("시작역ID로 즐겨찾기 삭제")
    @SuppressWarnings("all")
    void deleteBySourceId() {
        Station station1 = new Station("1번역");
        Station station2 = new Station("2번역");
        Station savedStation1 = stationRepository.save(station1);
        Station savedStation2 = stationRepository.save(station2);

        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(savedStation1.getId(), savedStation2.getId()));
        Member savedMember = memberRepository.save(member);

        favoriteRepository.deleteBySourceId(savedStation1.getId());
        savedMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(savedMember.getFavorites().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("도착역ID로 즐겨찾기 삭제")
    @SuppressWarnings("all")
    void deleteByTargetId() {
        Station station1 = new Station("1번역");
        Station station2 = new Station("2번역");
        Station savedStation1 = stationRepository.save(station1);
        Station savedStation2 = stationRepository.save(station2);

        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(savedStation1.getId(), savedStation2.getId()));
        Member savedMember = memberRepository.save(member);

        favoriteRepository.deleteByTargetId(savedStation2.getId());
        savedMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(savedMember.getFavorites().size()).isEqualTo(0);
    }
}