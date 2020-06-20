package wooteco.subway.domain.favorite;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
	@Query("SELECT * FROM favorite WHERE member_id =:id")
	List<Favorite> findByMemberId(@Param("id") Long id);
}
