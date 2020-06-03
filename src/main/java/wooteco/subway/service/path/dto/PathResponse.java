package wooteco.subway.service.path.dto;

import java.util.List;

import wooteco.subway.service.station.dto.StationResponse;

public class PathResponse {
	private List<StationResponse> stations;
	private int duration;
	private int distance;

	public PathResponse() {
	}

	public PathResponse(List<StationResponse> stations, int duration, int distance) {
		this.stations = stations;
		this.duration = duration;
		this.distance = distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDuration() {
		return duration;
	}

	public int getDistance() {
		return distance;
	}
}
