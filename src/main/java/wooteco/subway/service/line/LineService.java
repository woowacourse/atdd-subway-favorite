package wooteco.subway.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

import java.util.List;

@Service
public class LineService {
    private LineStationService lineStationService;
    private LineRepository lineRepository;

    public LineService(LineStationService lineStationService, LineRepository lineRepository) {
        this.lineStationService = lineStationService;
        this.lineRepository = lineRepository;
    }

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    @Transactional
    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse retrieveLine(Long id) {
        return lineStationService.findLineWithStationsById(id);
    }

    @Transactional
    public WholeSubwayResponse findLinesWithStations() {
        return lineStationService.findLinesWithStations();
    }
}
