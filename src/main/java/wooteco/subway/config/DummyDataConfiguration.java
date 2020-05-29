package wooteco.subway.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.MemberService;

import java.time.LocalTime;

@Configuration
public class DummyDataConfiguration {
    @Configuration
    @Profile("local")
    private class LocalDataApplicationRunner implements ApplicationRunner {
        private final MemberService memberService;
        private final StationRepository stationRepository;
        private final LineRepository lineRepository;
        private final FavoriteRepository favoriteRepository;

        public LocalDataApplicationRunner(MemberService memberService, StationRepository stationRepository, LineRepository lineRepository, FavoriteRepository favoriteRepository) {
            this.memberService = memberService;
            this.stationRepository = stationRepository;
            this.lineRepository = lineRepository;
            this.favoriteRepository = favoriteRepository;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            Station station1 = stationRepository.save(new Station("중랑"));
            Station station2 = stationRepository.save(new Station("청량리"));
            Station station3 = stationRepository.save(new Station("왕십리"));

            Line lineNumber1 = new Line("경의중앙선", LocalTime.of(05, 10), LocalTime.of(22, 10), 10);
            lineNumber1.addLineStation(new LineStation(null, station1.getId(), 10, 10));
            lineNumber1.addLineStation(new LineStation(station1.getId(), station2.getId(), 1, 10));
            lineNumber1.addLineStation(new LineStation(station2.getId(), station3.getId(), 10, 10));
            Line lineNumber2 = new Line("분당선", LocalTime.of(05, 20), LocalTime.of(23, 30), 15);
            lineNumber2.addLineStation(new LineStation(null, station2.getId(), 10, 10));
            lineNumber2.addLineStation(new LineStation(station2.getId(), station3.getId(), 1, 10));

            lineNumber1 = lineRepository.save(lineNumber1);
            lineNumber2 = lineRepository.save(lineNumber2);

            Station station4 = stationRepository.save(new Station("신촌"));
            Station station5 = stationRepository.save(new Station("합정"));
            Line lineNumber3 = new Line("2호선", LocalTime.of(05, 10), LocalTime.of(22, 10), 10);
            lineNumber3.addLineStation(new LineStation(null, station4.getId(), 10, 10));
            lineNumber3.addLineStation(new LineStation(station4.getId(), station5.getId(), 10, 10));

            lineNumber3 = lineRepository.save(lineNumber3);

            Member member = memberService.createMember(new Member("ramen6315@gmail.com", "ramen", "6315"));

            favoriteRepository.save(new Favorite(member.getId(), station1.getId(), station2.getId()));
            favoriteRepository.save(new Favorite(member.getId(), station2.getId(), station3.getId()));
        }
    }
}
