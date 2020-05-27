package wooteco.subway.acceptance;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.member.dto.TokenResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private final static String START_STATION_NAME = "신정역";
    private final static String END_STATION_NAME = "목동역";
    private TokenResponse tokenResponse;

    @BeforeEach
    void init() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    @Test
    void registerFavoriteSucceed() {
        Response response = registerFavorite();
        response.then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteFavorite() {
        registerFavorite();
        Map<String, String> params = new HashMap<>();
        params.put("startStationName", START_STATION_NAME);
        params.put("endStationName", END_STATION_NAME);

        given()
            .body(params)
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .log().all()
            .delete("/favorites")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private Response registerFavorite() {
        createStation(START_STATION_NAME);
        createStation(END_STATION_NAME);

        Map<String, String> params = new HashMap<>();
        params.put("startStationName", START_STATION_NAME);
        params.put("endStationName", END_STATION_NAME);

        return given()
            .body(params)
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .log().all()
            .post("/favorites");
    }
}
