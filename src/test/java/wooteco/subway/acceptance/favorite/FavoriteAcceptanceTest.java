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

import io.restassured.RestAssured;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.MemberDetailResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        station1 = new Station(1L, STATION_NAME_KANGNAM);
        station2 = new Station(2L, STATION_NAME_DOGOK);
        station3 = new Station(3L, STATION_NAME_SEOLLEUNG);
        station4 = new Station(4L, STATION_NAME_YEOKSAM);
    }

    @DisplayName("즐겨찾기 관리")
    @TestFactory
    public Stream<DynamicTest> manageFavorite() {
        return Stream.of(
            DynamicTest.dynamicTest("Create Favorite", () -> {
                createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
                    TEST_USER_PASSWORD);
                String location = createFavorite(TEST_USER_EMAIL, 1L, 2L);
                String location2 = createFavorite(TEST_USER_EMAIL, 1L, 4L);
                assertThat(location).isNotNull();
                assertThat(location2).isNotNull();
            }),
            DynamicTest.dynamicTest("Read Favorite", () -> {
                MemberDetailResponse memberDetailResponse = getDetailMember(TEST_USER_EMAIL);
                assertThat(memberDetailResponse.getFavorites())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(new Favorite(1L, 1L, 2L), new Favorite(2L, 1L, 4L));
            }),
            DynamicTest.dynamicTest("Delete Favorite", () -> {
                deleteFavorite(TEST_USER_EMAIL, 1L);
                MemberDetailResponse memberDetailResponse = getDetailMember(TEST_USER_EMAIL);
                Set<Favorite> favorites = memberDetailResponse.getFavorites();
                assertThat(favorites).hasSize(1);
                assertThat(favorites).usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(new Favorite(2L, 1L, 4L));
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

    private String createFavorite(String email, Long sourceId, Long targetId) {
        String token = jwtTokenProvider.createToken(email);
        Map<String, Long> params = new HashMap<>();
        params.put("sourceId", sourceId);
        params.put("targetId", targetId);

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
