package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        initStation();
    }

    @DisplayName("즐겨찾기 기능")
    @Test
    public void manageFavorite() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        PathResponse pathResponse = findPath(STATION_NAME_KANGNAM, STATION_NAME_DOGOK, "DISTANCE");

        Long source = pathResponse.getStations().get(0).getId();
        Long target = pathResponse.getStations().get(pathResponse.getStations().size() - 1).getId();

        String location = createFavorite(tokenResponse, source, target);
        List<FavoriteResponse> favorites = getFavorites(tokenResponse);
        assertThat(favorites).hasSize(1);

        deleteFavorite(tokenResponse, location);
    }

    private String createFavorite(TokenResponse tokenResponse, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return
                given().
                    auth().oauth2(tokenResponse.getAccessToken()).
                    body(params).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    post("/favorites").
                then().
                    log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    extract().header("Location");
    }

    private void deleteFavorite(TokenResponse tokenResponse, String location) {
        given().
                auth().oauth2(tokenResponse.getAccessToken()).
        when().
                delete(location).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    public List<FavoriteResponse> getFavorites(TokenResponse tokenResponse) {
        return
                given().
                        auth().oauth2(tokenResponse.getAccessToken()).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get("/favorites").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().jsonPath().getList(".", FavoriteResponse.class);
    }
}
