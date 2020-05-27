package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.service.member.dto.MemberDetailResponse;

@Sql("/truncate.sql")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_DOGOK);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_YEOKSAM);
    }

    @DisplayName("즐겨찾기 관리")
    @TestFactory
    public Stream<DynamicTest> manageFavorite() {
        return Stream.of(
            DynamicTest.dynamicTest("Create Favorite", () -> {
                createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
                    TEST_USER_PASSWORD);
                String location = createFavorite(TEST_USER_EMAIL, STATION_NAME_KANGNAM,
                    STATION_NAME_DOGOK);
                String location2 = createFavorite(TEST_USER_EMAIL, STATION_NAME_KANGNAM,
                    STATION_NAME_YEOKSAM);
                assertThat(location).isNotNull();
                assertThat(location2).isNotNull();
            }),
            DynamicTest.dynamicTest("Read Favorite", () -> {
                MemberDetailResponse memberDetailResponse = getDetailMember(TEST_USER_EMAIL);
                assertThat(memberDetailResponse.getFavorites()).extracting(
                    FavoriteResponse::getSourceId).containsExactlyInAnyOrder(1L, 1L);
                assertThat(memberDetailResponse.getFavorites()).extracting(
                    FavoriteResponse::getTargetId).containsExactlyInAnyOrder(4L, 2L);
            }),
            DynamicTest.dynamicTest("Delete Favorite", () -> {
                deleteFavorite(TEST_USER_EMAIL, 1L);
                MemberDetailResponse memberDetailResponse = getDetailMember(TEST_USER_EMAIL);
                Set<FavoriteResponse> favorites = memberDetailResponse.getFavorites();
                assertThat(favorites).hasSize(1);
                assertThat(favorites).usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(new FavoriteResponse(1L, 4L));
            })
        );
    }

    private void deleteFavorite(String email, Long favoriteId) {
        String token = jwtTokenProvider.createToken(email);
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .cookie("token", token)
            .when()
            .delete("/favorites/" + favoriteId)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private String createFavorite(String email, String sourceName, String targetName) {
        String token = jwtTokenProvider.createToken(email);
        Map<String, String> params = new HashMap<>();
        params.put("sourceName", sourceName);
        params.put("targetName", targetName);

        return given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            cookie("token", token).
            body(params).
            when().
            post("/favorites").
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            extract().header("Location");
    }

}
