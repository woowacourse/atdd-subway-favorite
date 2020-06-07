package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static wooteco.subway.web.FavoriteController.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @TestFactory
    public Stream<DynamicTest> manageFavorite() {
        // given 노선과 역, 구간이 주어진다
        LineResponse line = createLine(LINE_NAME_2);
        StationResponse kangnam = createStation(STATION_NAME_KANGNAM);
        StationResponse dogok = createStation(STATION_NAME_DOGOK);
        StationResponse hanti = createStation(STATION_NAME_HANTI);
        addLineStation(line.getId(), null, kangnam.getId());
        addLineStation(line.getId(), kangnam.getId(), dogok.getId());
        addLineStation(line.getId(), dogok.getId(), hanti.getId());

        // and 회원이 로그인한 상태이다
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        return Stream.of(dynamicTest("즐겨찾기 추가", () -> {
                // given 경로를 검색한 상태이다
                String departure = STATION_NAME_KANGNAM;
                String arrival = STATION_NAME_HANTI;
                // when 즐겨찾기를 저장하는 요청을 보낸다
                String location = addFavorite(tokenResponse, departure, arrival);

                // then 해당 회원에 즐겨찾기가 추가되었다
                assertThat(location).isNotNull();
            }),
            dynamicTest("즐겨찾기 목록 조회", () -> {
                // when 즐겨찾기 목록을 조회한다
                List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);

                // then 해당 회원의 즐겨찾기 목록을 반환한다
                assertThat(favoriteResponses.size()).isEqualTo(1);
            }),
            dynamicTest("즐겨찾기 삭제", () -> {
                // when 회원이 즐겨찾기 삭제를 요청한다
                String departure = STATION_NAME_KANGNAM;
                String arrival = STATION_NAME_HANTI;
                deleteFavorite(tokenResponse, departure, arrival);

                // then 해당 회원의 즐겨찾기가 삭제되었다
                List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);
                assertThat(favoriteResponses).isEmpty();
            }));
    }

    private String addFavorite(TokenResponse tokenResponse, String departure, String arrival) {
        Map<String, String> params = new HashMap<>();
        params.put("departure", departure);
        params.put("arrival", arrival);

        return given().
            header("Authorization",
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            when().
            post(FAVORITES_URI).
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            extract().header("Location");
    }

    private List<FavoriteResponse> getFavorites(TokenResponse tokenResponse) {
        return given().
            header("Authorization",
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get(FAVORITES_URI).
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().jsonPath().getList(".", FavoriteResponse.class);
    }

    private void deleteFavorite(TokenResponse tokenResponse, String departure, String arrival) {
        Map<String, String> params = new HashMap<>();
        params.put("departure", departure);
        params.put("arrival", arrival);

        given().
            header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            when().
            delete(FAVORITES_URI).
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }
}
