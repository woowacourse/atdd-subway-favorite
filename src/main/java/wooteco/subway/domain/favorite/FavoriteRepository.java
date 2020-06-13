package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("select * from favorite where source_id = :sourceId")
    Optional<Favorite> findBySourceId(@Param("sourceId") Long sourceId);

    @Query("select * from favorite where target_id = :targetId")
    Optional<Favorite> findByTargetId(@Param("targetId") Long targetId);

    @Modifying
    @Query("delete from favorite where source_id = :sourceId")
    void deleteBySourceId(@Param("sourceId") Long sourceId);

    @Modifying
    @Query("delete from favorite where target_id = :targetId")
    void deleteByTargetId(@Param("targetId") Long targetId);
}
