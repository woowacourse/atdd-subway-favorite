package wooteco.subway.domain.station;

import wooteco.subway.exception.NoStationExistsException;

import java.util.List;

public class Stations {
	private final List<Station> stations;

	private Stations(List<Station> stations) {
		this.stations = stations;
	}

	public static Stations of(List<Station> stations) {
		return new Stations(stations);
	}

	public List<Station> getStations() {
		return stations;
	}

	public Station extractStationById(Long stationId) {
		return stations.stream()
				.filter(station -> station.getId().equals(stationId))
				.findFirst()
				.orElseThrow(NoStationExistsException::new);
    }
}
