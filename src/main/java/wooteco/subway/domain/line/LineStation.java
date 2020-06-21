package wooteco.subway.domain.line;

import wooteco.subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class LineStation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn
	private Line line;
	@OneToOne
	@JoinColumn
	private Station preStation;
	@OneToOne
	@JoinColumn
	private Station station;
	private int distance;
	private int duration;

	protected LineStation() {
	}

	private LineStation(Long id, Line line, Station preStation, Station station, int distance, int duration) {
		this.id = id;
		this.line = line;
		this.preStation = preStation;
		this.station = station;
		this.distance = distance;
		this.duration = duration;
	}

	public static LineStation of(Station preStation, Station station, int distance, int duration) {
		return new LineStation(null, null, preStation, station, distance, duration);
	}

	public static LineStation of(Long id, Station preStation, Station station, int distance, int duration) {
		return new LineStation(id, null, preStation, station, distance, duration);
	}

	public void updatePreLineStation(Station preStation) {
		this.preStation = preStation;
	}

	public boolean isSameStationId(Long id) {
		return station.isSameId(id);
	}

	public boolean isLineStationOf(Long preStationId, Long stationId) {
		return this.preStation.getId() == preStationId && this.station.getId() == stationId
				|| this.preStation.getId() == stationId && this.station.getId() == preStationId;
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public Station getPreStation() {
		return preStation;
	}

	public Station getStation() {
		return station;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}

	public void setLine(Line line) {
		if (!line.getStations().contains(this)) {
			line.addLineStation(this);
			return;
		}
		this.line = line;
	}
}
