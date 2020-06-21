package wooteco.subway.domain.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
	@Override
	List<Station> findAllById(Iterable<Long> ids);

	@Override
	Optional<Station> findById(Long id);

	@Override
	List<Station> findAll();

	Optional<Station> findByName(String stationName);
}
