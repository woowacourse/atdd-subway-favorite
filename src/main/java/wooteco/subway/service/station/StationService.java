package wooteco.subway.service.station;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.LineStationService;
import wooteco.subway.service.member.MemberService;

@Service
public class StationService {
    private final LineStationService lineStationService;
    private final MemberService memberService;
    private final StationRepository stationRepository;

    public StationService(LineStationService lineStationService,
        MemberService memberService, StationRepository stationRepository) {
        this.lineStationService = lineStationService;
        this.memberService = memberService;
        this.stationRepository = stationRepository;
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    @Transactional
    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        memberService.deleteFavoriteByStationId(id);
        stationRepository.deleteById(id);
    }
}
