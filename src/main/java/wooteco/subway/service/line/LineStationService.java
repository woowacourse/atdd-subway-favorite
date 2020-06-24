package wooteco.subway.service.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStationRepository;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
public class LineStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineStationRepository lineStationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository,
        LineStationRepository lineStationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineStationRepository = lineStationRepository;
    }

    public LineStation save(LineStation lineStation) {
        return lineStationRepository.save(lineStation);
    }

    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);

        return LineDetailResponse.of(line);
    }

    public WholeSubwayResponse findLinesWithStations() {
        Lines lines = new Lines(lineRepository.findAll());

        List<LineDetailResponse> lineDetailResponses = lines.getLines().stream()
            .map(LineDetailResponse::of)
            .collect(Collectors.toList());

        return WholeSubwayResponse.of(lineDetailResponses);
    }

    @Transactional
    public void deleteLineStationByLineIdAndStationId(Long lindId, Long stationId) {
        lineStationRepository.deleteByLineIdAndStationId(lindId, stationId);
    }

    @Transactional
    public void deleteAllByStationId(Long stationId) {
        lineStationRepository.deleteAllByStationId(stationId);
    }

    public LineStation createLineStation(Long preStationId, Long stationId, int distance,
        int duration) {
        Station preStation = null;
        if (preStationId != null) {
            preStation = stationRepository.findById(preStationId)
                .orElseThrow(AssertionError::new);
        }
        Station station = stationRepository.findById(stationId)
            .orElseThrow(AssertionError::new);

        return new LineStation(preStation, station, distance, duration);
    }
}
