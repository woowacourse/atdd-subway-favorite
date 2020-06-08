package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    public void findAllByMemberId() {
        final Favorite favorite = new Favorite(10L, 2L, 3L);
        final Favorite favorite2 = new Favorite(10L, 7L, 10L);

        favoriteRepository.save(favorite);
        favoriteRepository.save(favorite2);

        final List<Favorite> favorites = favoriteRepository.findAllByMemberId(10L);

        assertThat(favorites).hasSize(2);
        assertThat(favorites.get(0).getMemberId()).isEqualTo(10L);
        assertThat(favorites.get(0).getSourceStationId()).isEqualTo(2L);
        assertThat(favorites.get(0).getTargetStationId()).isEqualTo(3L);
        assertThat(favorites.get(1).getMemberId()).isEqualTo(10L);
        assertThat(favorites.get(1).getSourceStationId()).isEqualTo(7L);
        assertThat(favorites.get(1).getTargetStationId()).isEqualTo(10L);
    }

    @Test
    public void deleteAllByMemberId() {
        final Favorite favorite = new Favorite(10L, 2L, 3L);
        final Favorite favorite2 = new Favorite(11L, 7L, 10L);

        favoriteRepository.save(favorite);
        favoriteRepository.save(favorite2);

        favoriteRepository.deleteAllByMemberId(10L);
        final Iterable<Favorite> favorites = favoriteRepository.findAll();

        assertThat(favorites).hasSize(1);
        Favorite favoriteEntity = favorites.iterator().next();
        assertThat(favoriteEntity.getMemberId()).isEqualTo(11L);
        assertThat(favoriteEntity.getSourceStationId()).isEqualTo(7L);
        assertThat(favoriteEntity.getTargetStationId()).isEqualTo(10L);
    }

    @Test
    public void existsBy() {
        final Favorite favorite = new Favorite(10L, 2L, 3L);
        final Favorite favorite2 = new Favorite(11L, 7L, 10L);

        favoriteRepository.save(favorite);
        favoriteRepository.save(favorite2);

        assertThat(favoriteRepository.existsBy(10L, 2L, 3L)).isTrue();
        assertThat(favoriteRepository.existsBy(10L, 2L, 4L)).isFalse();
    }
}