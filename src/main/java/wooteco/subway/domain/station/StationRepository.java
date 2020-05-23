package wooteco.subway.domain.station;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.subway.service.favorite.dto.FavoriteResponse;

public interface StationRepository extends CrudRepository<Station, Long> {
	@Override
	List<Station> findAllById(Iterable ids);

	@Override
	List<Station> findAll();

	@Query("select * from station where name = :stationName")
	Optional<Station> findByName(@Param("stationName") String stationName);

	@Query("select a.NAME, b.NAME"
		+ "from STATION as a"
		+ "     full outer join STATION as b"
		+ "     on ")
	List<FavoriteResponse> test(List<Long> sourceStationIds, List<Long> targetStationIds);
}
