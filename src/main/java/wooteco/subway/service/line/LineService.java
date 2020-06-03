package wooteco.subway.service.line;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.exceptions.NotExistLineException;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
@Transactional
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

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(() -> new NotExistLineException(id));
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NotExistLineException(id));
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new NotExistLineException(lineId));
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public LineDetailResponse retrieveLine(Long id) {
        return lineStationService.findLineWithStationsById(id);
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse findLinesWithStations() {
        return lineStationService.findLinesWithStations();
    }
}
