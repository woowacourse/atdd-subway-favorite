package wooteco.subway.service.member.dto;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateMemberRequestTest extends AcceptanceTest {

    @Test
    void updateWhenFail() {
        Long memberId = extractId(createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        assertThat(updateMemberWhenFail(memberId)).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Long extractId(String location) {
        List<String> path = Arrays.asList(location.split("/"));
        int idIndex = path.size() - 1;
        return Long.parseLong(path.get(idIndex));
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