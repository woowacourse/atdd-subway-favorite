package wooteco.subway.acceptance.member.favorite;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.favorite.dto.AddFavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.favorite.dto.FavoritesResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Test
    void favoriteAcceptanceTest() {
        // given : 역이 존재한다
        createStation("역1");
        createStation("역2");
        createStation("역3");

        // and : 멤버가 로그인을 했다.
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // when : 멤버가 즐겨찾기를 추가한다.
        MemberResponse memberResponse = getMember(TEST_USER_EMAIL, response.getSessionId(), getTokenResponse(response));
        addFavorite(memberResponse.getId(), new AddFavoriteRequest(1L, 2L), response);
        addFavorite(memberResponse.getId(), new AddFavoriteRequest(2L, 3L), response);

        // then : 즐겨찾기가 추가되었다.
        FavoritesResponse favoritesResponse = readFavorite(memberResponse.getId(), response);
        assertThat(favoritesResponse.getFavorites().size()).isEqualTo(2);

        // when : 멤버가 즐겨찾기를 삭제한다.
        removeFavorite(memberResponse.getId(), 1L, 2L, response);

        // then : 즐겨찾기가 삭제되었다.
        favoritesResponse = readFavorite(memberResponse.getId(), response);
        assertThat(favoritesResponse.getFavorites().size()).isEqualTo(1);
    }

    private RequestSpecification setAuthorization(Response loginResponse) {
        String sessionId = loginResponse.getSessionId();
        TokenResponse tokenResponse = getTokenResponse(loginResponse);

        return
                given().
                        cookie("JSESSIONID", sessionId).
                        header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
    }

    private FavoriteResponse addFavorite(Long memberId, AddFavoriteRequest addFavoriteRequest, Response loginResponse) {
        return
                setAuthorization(loginResponse).
                        body(addFavoriteRequest).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post("/members/" + memberId + "/favorites").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(FavoriteResponse.class);
    }

    private FavoritesResponse readFavorite(Long memberId, Response loginResponse) {
        return
                setAuthorization(loginResponse).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        get("/members/" + memberId + "/favorites").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().
                        as(FavoritesResponse.class);
    }

    private void removeFavorite(Long memberId, Long sourceId, Long targetId, Response loginResponse) {
        setAuthorization(loginResponse).
                when().
                delete("/members/" + memberId + "/favorites/source/" + sourceId + "/target/" + targetId).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }
}
