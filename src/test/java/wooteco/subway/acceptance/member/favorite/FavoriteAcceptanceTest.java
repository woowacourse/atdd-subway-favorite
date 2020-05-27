package wooteco.subway.acceptance.member.favorite;

import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.FavoriteResponses;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.web.dto.DefaultResponse;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 추가, 조회, 삭제 인수테스트")
    @TestFactory
    Stream<DynamicTest> manageFavorite() {
        return Stream.of(
                dynamicTest("addFavorite", () -> {
                    createStation("잠실");
                    createStation("역삼");
                    List<StationResponse> stations = getStations();
                    createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);

                    FavoriteRequest favoriteRequest = new FavoriteRequest(stations.get(0).getId(), stations.get(1).getId());

                    given().
                            auth().
                            oauth2(tokenResponse.getAccessToken()).
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            body(favoriteRequest).
                            when().
                            post("/me/favorites").
                            then().
                            log().all()
                            .statusCode(HttpStatus.CREATED.value());

                }),
                dynamicTest("getFavorites", () -> {
                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                    FavoriteResponses favoriteResponses = getFavoriteResponses(tokenResponse);
                    List<FavoriteResponse> favorites = favoriteResponses.getFavoriteResponses();

                    assertThat(favorites).hasSize(1);
                    assertThat(favorites.get(0).getSourceStationId()).isEqualTo(1L);
                    assertThat(favorites.get(0).getTargetStationId()).isEqualTo(2L);
                }),
                dynamicTest("deleteFavorite", () -> {
                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                    FavoriteResponses favoriteResponses = getFavoriteResponses(tokenResponse);
                    List<FavoriteResponse> favorites = favoriteResponses.getFavoriteResponses();

                    FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest(favorites.get(0).getSourceStationId(), favorites.get(0).getTargetStationId());

                    given().
                            auth().
                            oauth2(tokenResponse.getAccessToken()).
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            body(favoriteDeleteRequest).
                            when().
                            delete("/me/favorites").
                            then().
                            log().all().
                            statusCode(HttpStatus.NO_CONTENT.value());

                    FavoriteResponses favoriteResponses1 = getFavoriteResponses(tokenResponse);
                    assertThat(favoriteResponses1.getFavoriteResponses()).isEmpty();
                })
        );
    }

    private FavoriteResponses getFavoriteResponses(final TokenResponse tokenResponse) {
        return given().
                auth().
                oauth2(tokenResponse.getAccessToken()).
                when().
                get("/me/favorites").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(new TypeRef<DefaultResponse<FavoriteResponses>>() {
        }).getData();
    }
}
