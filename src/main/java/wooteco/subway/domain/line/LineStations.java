package wooteco.subway.domain.line;

import wooteco.subway.domain.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class LineStations {
	@OneToMany(mappedBy = "line", orphanRemoval = true)
	private Set<LineStation> stations = new LinkedHashSet<>();

	protected LineStations() {
	}

	private LineStations(Set<LineStation> stations) {
		this.stations = stations;
	}

	public static LineStations empty() {
		return new LineStations();
	}

	public void add(LineStation targetLineStation) {
		updatePreStationOfNextLineStation(targetLineStation.getPreStation(), targetLineStation.getStation());
		stations.add(targetLineStation);
	}

	private void remove(LineStation targetLineStation) {
		updatePreStationOfNextLineStation(targetLineStation.getStation(), targetLineStation.getPreStation());
		stations.remove(targetLineStation);
	}

	public void removeById(Long targetStationId) {
		extractByStationId(targetStationId)
			.ifPresent(this::remove);
	}

	public List<Station> getSortedStations() {
		List<Station> result = new ArrayList<>();
		extractNext(null, result);
		return result;
	}

	private void extractNext(Station preStation, List<Station> tempStations) {
		stations.stream()
			.filter(it -> Objects.equals(it.getPreStation(), preStation))
			.findFirst()
			.ifPresent(it -> {
				Station station = it.getStation();
				tempStations.add(station);
				extractNext(station, tempStations);
			});
	}

	private void updatePreStationOfNextLineStation(Station targetStation, Station newPreStation) {
		extractByPreStation(targetStation)
			.ifPresent(it -> it.updatePreLineStation(newPreStation));
	}

	private Optional<LineStation> extractByStationId(Long stationId) {
		return stations.stream()
				.filter(it -> it.isSameStationId(stationId))
				.findFirst();
	}

	private Optional<LineStation> extractByPreStation(Station preStation) {
		return stations.stream()
			.filter(it -> Objects.equals(it.getPreStation(), preStation))
			.findFirst();
	}

	public Set<LineStation> getStations() {
		return stations;
	}

	public int getTotalDistance() {
		return stations.stream().mapToInt(LineStation::getDistance).sum();
	}

	public int getTotalDuration() {
		return stations.stream().mapToInt(LineStation::getDuration).sum();
	}
}
