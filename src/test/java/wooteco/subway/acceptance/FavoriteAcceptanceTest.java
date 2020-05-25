package wooteco.subway.acceptance;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.member.dto.TokenResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Test
    void registerFavorite() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        String startStationName = "신정역";
        String endStationName = "목동역";
        createStation(startStationName);
        createStation(endStationName);

        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        Map<String, String> params = new HashMap<>();
        params.put("startStationName", startStationName);
        params.put("endStationName", endStationName);

        given()
            .body(params)
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .log().all()
            .post("/favorites")
        .then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
    }
}
