package wooteco.subway.service.station.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import wooteco.subway.domain.station.Station;

public class StationCreateRequest {

	@NotEmpty
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public Station toStation() {
		return new Station(name);
	}
}
