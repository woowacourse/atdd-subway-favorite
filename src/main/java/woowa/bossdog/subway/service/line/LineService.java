package woowa.bossdog.subway.service.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.domain.LineStation;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.LineRepository;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.line.dto.*;
import woowa.bossdog.subway.web.station.NotExistedStationException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse createLine(final LineRequest request) {
        final Line line = request.toLine();
        lineRepository.save(line);
        return LineResponse.from(line);
    }

    public List<LineResponse> listLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(final Long id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(final Long id, final UpdateLineRequest request) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        line.update(request);
    }

    @Transactional
    public void deleteLine(final Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(final Long lineId, final LineStationRequest request) {
        final Line line = lineRepository.findById(lineId).orElseThrow(NoSuchElementException::new);
        final Station requestPreStation = Objects.isNull(request.getPreStationId()) ? null
                : stationRepository.findById(request.getPreStationId()).orElse(null);
        final Station requestStation = stationRepository.findById(request.getStationId())
                .orElseThrow(NotExistedStationException::new);

        if (request.getPreStationId() == null | line.getStations().contains(requestPreStation)) {
            line.getLineStations().stream()
                    .filter(ls -> Objects.equals(ls.getPreStation(), requestPreStation))
                    .findFirst()
                    .ifPresent(ls -> ls.updatePreStation(requestStation));

            line.getLineStations().add(new LineStation(line, requestPreStation,
                    requestStation, request.getDistance(), request.getDuration()));
        }
    }

    @Transactional
    public void deleteLineStation(final Long id, final Long stationId) {
        final Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        final Station station = stationRepository.findById(stationId).orElseThrow(NotExistedStationException::new);

        List<LineStation> lineStations = line.getLineStations();
        LineStation target = lineStations.stream()
                .filter(ls -> Objects.equals(ls.getStation(), station))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        line.getLineStations().stream()
                .filter(ls -> Objects.equals(ls.getPreStation(), station))
                .findFirst()
                .ifPresent(ls -> ls.updatePreStation(target.getPreStation()));
        line.getLineStations().remove(target);
    }

    public LineDetailResponse findLineDetail(final Long id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return LineDetailResponse.of(line, line.getStations());
    }

    public WholeSubwayResponse listLineDetail() {
        List<Line> lines = lineRepository.findAll();
        final List<LineDetailResponse> collect = lines.stream()
                .map(line -> findLineDetail(line.getId()))
                .collect(Collectors.toList());
        return new WholeSubwayResponse(collect);
    }
}
