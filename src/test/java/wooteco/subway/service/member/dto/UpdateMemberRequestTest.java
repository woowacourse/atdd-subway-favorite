package wooteco.subway.service.member.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;

class UpdateMemberRequestTest extends AcceptanceTest {

    @Test
    void updateWhenFail() {
        Long memberId = extractId(createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        assertThat(updateMemberWhenFail(memberId)).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public int updateMemberWhenFail(Long id) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "");
        params.put("password", "");

        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        put("/members/" + id).
                        then().
                        log().all().
                        extract().statusCode();
    }
}