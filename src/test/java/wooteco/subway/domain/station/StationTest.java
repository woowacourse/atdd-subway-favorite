package wooteco.subway.domain.station;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StationTest {

    @Test
    public void construct() {
        Station 강남역 = new Station("강남역");
        Station 한티역 = new Station(3L, "한티역");

        assertThat(강남역).isNotNull();
        assertThat(강남역.getName()).isEqualTo("강남역");
        assertThat(강남역.getCreatedAt()).isNotNull();


        assertThat(한티역).isNotNull();
        assertThat(한티역.getId()).isEqualTo(3L);
        assertThat(한티역.getName()).isEqualTo("한티역");
        assertThat(한티역.getCreatedAt()).isNotNull();
    }

}