package wooteco.subway.domain.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LineStations {
	private final Set<LineStation> stations;

	LineStations(Set<LineStation> stations) {
		this.stations = stations;
	}

	public static LineStations empty() {
		return new LineStations(new HashSet<>());
	}

	public Set<LineStation> getStations() {
		return stations;
	}

	public void add(LineStation targetLineStation) {
		updatePreStationOfNextLineStation(targetLineStation.getPreStationId(), targetLineStation.getStationId());
		stations.add(targetLineStation);
	}

	private void remove(LineStation targetLineStation) {
		updatePreStationOfNextLineStation(targetLineStation.getStationId(), targetLineStation.getPreStationId());
		stations.remove(targetLineStation);
	}

	public void removeById(Long targetStationId) {
		extractByStationId(targetStationId)
			.ifPresent(this::remove);
	}

	public List<Long> getStationIds() {
		List<Long> result = new ArrayList<>();
		Long currentStationId = null;
		Optional<LineStation> currentLineStation = extractNext(currentStationId);

		while (currentLineStation.isPresent()) {
			currentStationId = currentLineStation.get().getStationId();
			result.add(currentStationId);
			currentLineStation = extractNext(currentStationId);
		}
		return result;
	}

	private Optional<LineStation> extractNext(Long preStationId) {
		return stations.stream()
			.filter(station -> Objects.equals(station.getPreStationId(), preStationId))
			.findFirst();
	}

	private void updatePreStationOfNextLineStation(Long targetStationId, Long newPreStationId) {
		extractByPreStationId(targetStationId)
			.ifPresent(it -> {
				final LineStation lineStation = it.updatePreLineStation(newPreStationId);
				stations.remove(it);
				stations.add(lineStation);
			});
	}

	private Optional<LineStation> extractByStationId(Long stationId) {
		return stations.stream()
			.filter(it -> Objects.equals(it.getStationId(), stationId))
			.findFirst();
	}

	private Optional<LineStation> extractByPreStationId(Long preStationId) {
		return stations.stream()
			.filter(it -> Objects.equals(it.getPreStationId(), preStationId))
			.findFirst();
	}

	public int getTotalDistance() {
		return stations.stream().mapToInt(LineStation::getDistance).sum();
	}

	public int getTotalDuration() {
		return stations.stream().mapToInt(LineStation::getDuration).sum();
	}
}
