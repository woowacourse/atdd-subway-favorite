package wooteco.subway.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionControllerTest extends AcceptanceTest {
    @Test
    void createMemberWithDuplicateEmail() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        ErrorResponse errorResponse = createDuplicateMember();

        assertThat(errorResponse).isNotNull();
    }

    private ErrorResponse createDuplicateMember() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_USER_EMAIL);
        params.put("name", "pobi");
        params.put("password", "123");

        return given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members").
                then().
                log().all().
                extract().as(ErrorResponse.class);
    }
}