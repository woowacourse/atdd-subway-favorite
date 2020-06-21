package wooteco.subway.service.line;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStationRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LineService {
	private final LineStationService lineStationService;
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final LineStationRepository lineStationRepository;

	public LineService(LineStationService lineStationService, LineRepository lineRepository, StationRepository stationRepository, LineStationRepository lineStationRepository) {
		this.lineStationService = lineStationService;
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.lineStationRepository = lineStationRepository;
	}

	public Line save(Line line) {
		return lineRepository.save(line);
	}

	public List<Line> findLines() {
		return lineRepository.findAll();
	}

	public Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public Station findPreStationById(Long id) {
		if (Objects.isNull(id)) {
			return null;
		}
		return findStationById(id);
	}

	public Station findStationById(Long id) {
		return stationRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public void updateLine(Long id, LineRequest request) {
		Line persistLine = findLineById(id);
		persistLine.update(request.toLine());
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long id, LineStationCreateRequest request) {
		Line line = findLineById(id);
		LineStation lineStation = LineStation.of(findPreStationById(request.getPreStationId()), findStationById(request.getStationId()),
				request.getDistance(), request.getDuration());
		LineStation persist = lineStationRepository.save(lineStation);
		line.addLineStation(persist);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
		line.removeLineStationById(stationId);
	}

	public LineDetailResponse retrieveLine(Long id) {
		return lineStationService.findLineWithStationsById(id);
	}

	public WholeSubwayResponse findLinesWithStations() {
		return lineStationService.findLinesWithStations();
	}
}
