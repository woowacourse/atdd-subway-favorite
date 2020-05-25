package wooteco.subway.domain.favorite;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
	@Query("SELECT * FROM FAVORITE WHERE member_id=:memberId")
	List<Favorite> findByMemberId(@Param("memberId") Long memberId);

	@Modifying
	@Query("DELETE FROM FAVORITE WHERE member_id=:memberId AND id=:favoriteId")
	void deleteByMemberIdAndId(@Param("memberId") Long memberId, @Param("favoriteId") Long favoriteId);

	@Modifying
	@Query("DELETE FROM FAVORITE WHERE member_id=:memberId")
	void deleteAllByMemberId(@Param("memberId") Long memberId);
}
