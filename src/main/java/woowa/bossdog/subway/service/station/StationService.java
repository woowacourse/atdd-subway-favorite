package woowa.bossdog.subway.service.station;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.station.dto.StationRequest;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    @Transactional
    public StationResponse createStation(final StationRequest request) {
        final Station station = request.toStation();
        stationRepository.save(station);
        return StationResponse.from(station);
    }

    public List<StationResponse> listStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public StationResponse findStation(final Long id) {
        final Station station = stationRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        return StationResponse.from(station);
    }

    @Transactional
    public void deleteStation(final Long id) {
        stationRepository.deleteById(id);
    }
}
