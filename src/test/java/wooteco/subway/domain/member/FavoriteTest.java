package wooteco.subway.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {
    @DisplayName("(예외) 출발역과 도착역이 같을 때")
    @Test
    void constructException() {
        assertThatThrownBy(() -> new Favorite("삼성역", "삼성역"))
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }
}
