package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        initStation();
    }

    @Test
    void manageFavorite() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(1L, 1L, 3L);
        addFavorite(tokenResponse, favoriteCreateRequest);

        List<FavoriteResponse> allFavorites = getAllFavorites(tokenResponse);
        assertThat(allFavorites.size()).isEqualTo(1);
        assertThat(allFavorites.get(0).getId()).isNotNull();
        assertThat(allFavorites.get(0).getSourceStationId()).isNotNull();
        assertThat(allFavorites.get(0).getSourceStationName()).isNotNull();
        assertThat(allFavorites.get(0).getTargetStationId()).isNotNull();
        assertThat(allFavorites.get(0).getTargetStationName()).isNotNull();

        deleteFavorite(tokenResponse);
    }

    private void addFavorite(TokenResponse tokenResponse, FavoriteCreateRequest favoriteCreateRequest) {
        given().
                auth().
                oauth2(tokenResponse.getAccessToken()).
                body(favoriteCreateRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/me/favorites").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    private List<FavoriteResponse> getAllFavorites(TokenResponse tokenResponse) {
        return
                given().
                        auth().
                        oauth2(tokenResponse.getAccessToken()).
                        when().
                        get("/me/favorites").
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", FavoriteResponse.class);
    }

    private void deleteFavorite(TokenResponse tokenResponse) {
        given().
                auth().
                oauth2(tokenResponse.getAccessToken()).
                when().
                delete("/me/favorites/1").
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }
}
