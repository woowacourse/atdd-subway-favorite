package wooteco.subway.acceptance.member.favorite;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.acceptance.Authentication;
import wooteco.subway.service.member.favorite.dto.AddFavoriteRequest;
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
                DynamicTest.dynamicTest("이미 등록된 즐겨찾기를 재 등록시도", () -> {
                    // when 멤버가 즐겨찾기를 다시 추가하려 한다
                    // then 실패한다
                    failToAddFavorite(memberId, new AddFavoriteRequest(1L, 2L), authentication);
                }),
                DynamicTest.dynamicTest("즐겨찾기 삭제", () -> {
                    // when 멤버가 즐겨찾기를 삭제한다
                    removeFavorite(memberId, 1L, 2L, authentication);

                    // then 즐겨찾기가 삭제되었다
                    FavoritesResponse favoritesResponse = readFavorite(memberId, authentication);
                    assertThat(favoritesResponse.getFavorites().size()).isEqualTo(1);
                }),
                DynamicTest.dynamicTest("즐겨찾기 삭제 실패(존재 하지 않는 즐겨찾기)", () -> {
                    // when 멤버가 존재하지 않는 즐겨찾기를 삭제한다
                    // then 삭제가 실패한다
                    failToRemoveFavorite(memberId, 1L, 2L, authentication);
                })
        );
    }
}
