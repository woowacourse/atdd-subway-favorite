package wooteco.subway.study;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubstringTest {

    @Test
    void substring() {
        String value = "Basic kdakdfjkejkrjkldla";
        String type = "Basic";
        String trim = value.substring(type.length()).trim();
        assertThat(trim).isEqualTo("kdakdfjkejkrjkldla");
    }
}
