package wooteco.subway.service.station;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.station.dto.StationCreateRequest;

@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    public Station createStation(StationCreateRequest request) {
        return stationRepository.save(request.toStation());
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    // public Long findStationIdByName(String name) {
    //     return stationRepository.findByName(name)
    //         .stream()
    //         .map(Station::getId)
    //         .findAny()
    //         .orElseThrow(() -> new NotFoundStationException(name + "역을 찾을 수 없습니다."));
    // }

    public List<Station> findAllById(Collection<Long> ids) {
        return stationRepository.findAllById(ids);
    }

    public List<Station> findAllByStationName(List<String> stations) {
        return stationRepository.findAllByStationName(stations);
    }
}
