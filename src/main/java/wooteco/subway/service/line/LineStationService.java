package wooteco.subway.service.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
public class LineStationService {
	private LineRepository lineRepository;

	public LineStationService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public LineDetailResponse findLineWithStationsById(Long lineId) {
		Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);

		return LineDetailResponse.of(line, line.getSortedStations());
	}

	public WholeSubwayResponse findLinesWithStations() {
		Lines lines = new Lines(lineRepository.findAll());
		List<LineDetailResponse> lineDetailResponses = lines.getLines().stream()
			.map(it -> LineDetailResponse.of(it, it.getSortedStations()))
			.collect(Collectors.toList());

		return WholeSubwayResponse.of(lineDetailResponses);
	}

	public void deleteLineStationByStationId(Long stationId) {
		List<Line> lines = lineRepository.findAll();
		lines.stream().forEach(it -> it.removeLineStationById(stationId));
	}
}
