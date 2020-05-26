package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

}
