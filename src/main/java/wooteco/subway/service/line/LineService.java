package wooteco.subway.service.line;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
public class LineService {
    private final LineStationService lineStationService;
    private final LineRepository lineRepository;

    public LineService(LineStationService lineStationService, LineRepository lineRepository) {
        this.lineStationService = lineStationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line save(Line line) {
        return lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line persistLine = lineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("노선을 찾을 수 없습니다."));
        persistLine.update(lineRequest.toLine());
        lineRepository.save(persistLine);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long id, LineStationCreateRequest lineStationCreateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("노선을 찾을 수 없습니다."));
        LineStation lineStation = new LineStation(lineStationCreateRequest.getPreStationId(),
                lineStationCreateRequest.getStationId(),
                lineStationCreateRequest.getDistance(), lineStationCreateRequest.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("노선을 찾을 수 없습니다."));
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
