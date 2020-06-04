package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.path.DuplicatedStationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {
    @DisplayName("Favoirte 생성")
    @Test
    public void construct() {
        Favorite favorite = new Favorite(2L, 3L, 4L);
        assertThat(favorite).isNotNull();
        assertThat(favorite.getMemberId()).isEqualTo(2L);
        assertThat(favorite.getSourceStationId()).isEqualTo(3L);
        assertThat(favorite.getTargetStationId()).isEqualTo(4L);
    }

    @DisplayName("station id가 같을 경우 예외 발생")
    @Test
    public void constructWithInvalidAttributes() {
        assertThatThrownBy(() -> {
            Favorite favorite = new Favorite(2L, 3L, 3L);
        }).isInstanceOf(DuplicatedStationException.class);
    }

    @Test
    public void updateStationsName() {
        Favorite favorite = new Favorite(2L, 3L, 4L);

        favorite.updateStationsName("강남역", "선릉역");
        assertThat(favorite.getSourceStationName()).isEqualTo("강남역");
        assertThat(favorite.getTargetStationName()).isEqualTo("선릉역");
    }

}