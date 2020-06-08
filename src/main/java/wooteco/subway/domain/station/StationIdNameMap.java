package wooteco.subway.domain.station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationIdNameMap {
	final Map<Long, String> stationIdNameMap;

	private StationIdNameMap(Map<Long, String> stationIdNameMap) {
		this.stationIdNameMap = stationIdNameMap;
	}

	public static StationIdNameMap of(List<Station> stations) {
		Map<Long, String> stationIdNameMap = new HashMap<>();
		for (Station station : stations) {
			stationIdNameMap.put(station.getId(), station.getName());
		}
		return new StationIdNameMap(stationIdNameMap);
	}

	public String getNameById(long sourceId) {
		return stationIdNameMap.get(sourceId);
	}
}
