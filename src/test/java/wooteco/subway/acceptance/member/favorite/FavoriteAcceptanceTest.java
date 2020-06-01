package wooteco.subway.acceptance.member.favorite;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.acceptance.Authentication;
import wooteco.subway.service.member.favorite.dto.AddFavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.favorite.dto.FavoritesResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기 관리 기능")
    @TestFactory
    Stream<DynamicTest> favoriteAcceptanceTest() {
        // given 역이 존재한다
        createStation("역1");
        createStation("역2");
        createStation("역3");

        // and 유저가 회원 가입했다
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Long memberId = Long.parseLong(location.split("/")[2]);

        //and 유저가 로그인 했다
        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        Authentication authentication = Authentication.of(response.getSessionId(), getTokenResponse(response));

        return Stream.of(
                DynamicTest.dynamicTest("즐겨찾기 추가", () -> {
                    // when 멤버가 즐겨찾기를 추가한다
                    addFavorite(memberId, new AddFavoriteRequest(1L, 2L), authentication);
                    addFavorite(memberId, new AddFavoriteRequest(2L, 3L), authentication);

                    // then 즐겨찾기가 추가되었다
                    FavoritesResponse favoritesResponse = readFavorite(memberId, authentication);
                    assertThat(favoritesResponse.getFavorites().size()).isEqualTo(2);
                }),
                DynamicTest.dynamicTest("즐겨찾기 삭제", () -> {
                    // when 멤버가 즐겨찾기를 삭제한다
                    removeFavorite(memberId, 1L, 2L, authentication);

                    // then 즐겨찾기가 삭제되었다
                    FavoritesResponse favoritesResponse = readFavorite(memberId, authentication);
                    assertThat(favoritesResponse.getFavorites().size()).isEqualTo(1);
                })
        );
    }

    private FavoriteResponse addFavorite(Long memberId, AddFavoriteRequest addFavoriteRequest, Authentication authentication) {
        return
                setAuthorization(authentication).
                        body(addFavoriteRequest).
                        when().
                        post("/members/" + memberId + "/favorites").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(FavoriteResponse.class);
    }

    private FavoritesResponse readFavorite(Long memberId, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members/" + memberId + "/favorites").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().
                        as(FavoritesResponse.class);
    }

    private void removeFavorite(Long memberId, Long sourceId, Long targetId, Authentication authentication) {
        setAuthorization(authentication).
                when().
                delete("/members/" + memberId + "/favorites/source/" + sourceId + "/target/" + targetId).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }
}
