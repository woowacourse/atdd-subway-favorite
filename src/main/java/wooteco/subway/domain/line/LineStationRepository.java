package wooteco.subway.domain.line;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {
    void deleteAllByStationId(Long stationId);

    void deleteAllByPreStationId(Long stationId);
}
