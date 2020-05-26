package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteRemoveRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private TokenResponse tokenResponse;

    @BeforeEach
    void login() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    @Test
    void manageFavorites() {
        FavoriteCreateRequest YONGSAN_TO_JAMSIL = new FavoriteCreateRequest(2L, 4L);
        addFavorite(YONGSAN_TO_JAMSIL, tokenResponse);

        FavoriteCreateRequest SEOUL_TO_GANGNAM = new FavoriteCreateRequest(1L, 9L);
        addFavorite(SEOUL_TO_GANGNAM, tokenResponse);

        List<FavoriteResponse> addedFavorites = getFavorites(tokenResponse);
        assertThat(addedFavorites.size()).isEqualTo(2);

        // FavoriteRemoveRequest favoriteRemoveRequest = new FavoriteRemoveRequest(2L, 4L);
        // removeFavorite(favoriteRemoveRequest, tokenResponse);
        //
        // List<FavoriteResponse> removedFavorites = getFavorites(tokenResponse);
        // assertThat(removedFavorites.size()).isEqualTo(1);
    }

    // @formatter:off
    public void addFavorite(FavoriteCreateRequest favoriteCreateRequest, TokenResponse tokenResponse) {
        given().
                auth().
                oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(favoriteCreateRequest).
        when().
                post("/members/favorites").
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public List<FavoriteResponse> getFavorites(TokenResponse tokenResponse) {
        return
                given().
                        auth().
                        oauth2(tokenResponse.getAccessToken()).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get("/members/favorites").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().
                        jsonPath().getList(".", FavoriteResponse.class);
    }

    public void removeFavorite(FavoriteRemoveRequest favoriteRemoveRequest, TokenResponse tokenResponse) {
        given().
                auth().
                oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                body(favoriteRemoveRequest).
        when().
                delete("/members/favorites").
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }
    // @formatter:on
}
