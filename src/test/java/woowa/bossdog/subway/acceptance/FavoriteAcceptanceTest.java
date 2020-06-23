package woowa.bossdog.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowa.bossdog.subway.service.member.dto.MemberResponse;
import woowa.bossdog.subway.service.member.dto.TokenResponse;
import woowa.bossdog.subway.service.favorite.dto.FavoriteRequest;
import woowa.bossdog.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    /**
     * 시나리오
     * 1. 지하철 역을 생성한다.
     * 2. 회원 가입한다.
     * 3. 로그인 한다.
     * 4. 경로 조회 한 결과를 즐겨찾기에 추가 한다. (실제로 경로 조회 할 필요 없음)
     * 5. 나의 즐겨찾기 목록을 불러온다.
     * 6. 즐겨찾기를 삭제한다.
     */

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        // 1. 지하철 역 생성
        createStation("강남역");
        createStation("선릉역");
        createStation("역삼역");
        createStation("삼성역");
        createStation("잠실역");
        createStation("구의역");
        createStation("시청역");

        // 2. 회원 가입
        createMember("test@test.com", "bossdog", "test");
        createMember("one@one.com", "onedog", "test");

        // 3. 로그인
        TokenResponse bossdogToken = loginMember("test@test.com", "test");
        TokenResponse onedogToken = loginMember("one@one.com", "test");


        // 4. 즐겨찾기 추가
        createFavorite(bossdogToken.getAccessToken(), 3L, 4L);
        createFavorite(bossdogToken.getAccessToken(), 1L, 7L);
        createFavorite(onedogToken.getAccessToken(), 2L, 5L);
        createFavorite(onedogToken.getAccessToken(), 4L, 6L);

        // 5. 즐겨찾기 목록 조회
        List<FavoriteResponse> bossdogResponses = listFavorites(bossdogToken.getAccessToken());
        assertThat(bossdogResponses).hasSize(2);
        assertThat(bossdogResponses.get(0).getSource().getName()).isEqualTo("역삼역");
        assertThat(bossdogResponses.get(0).getTarget().getName()).isEqualTo("삼성역");
        assertThat(bossdogResponses.get(1).getSource().getName()).isEqualTo("강남역");
        assertThat(bossdogResponses.get(1).getTarget().getName()).isEqualTo("시청역");

        List<FavoriteResponse> onedogResponses = listFavorites(onedogToken.getAccessToken());
        assertThat(onedogResponses).hasSize(2);
        assertThat(onedogResponses.get(0).getSource().getName()).isEqualTo("선릉역");
        assertThat(onedogResponses.get(0).getTarget().getName()).isEqualTo("잠실역");
        assertThat(onedogResponses.get(1).getSource().getName()).isEqualTo("삼성역");
        assertThat(onedogResponses.get(1).getTarget().getName()).isEqualTo("구의역");

        // 6. 즐겨찾기 삭제
        deleteFavorite(bossdogToken.getAccessToken(), bossdogResponses.get(0).getId());
        bossdogResponses = listFavorites(bossdogToken.getAccessToken());
        assertThat(bossdogResponses).hasSize(1);
        assertThat(bossdogResponses.get(0).getSource().getName()).isEqualTo("강남역");
        assertThat(bossdogResponses.get(0).getTarget().getName()).isEqualTo("시청역");

        // 6. 데이터 롤백
        final List<MemberResponse> members = listMembers();
        deleteMember(members.get(0).getId());
        deleteMember(members.get(1).getId());
        LongStream.range(1L, 8L).forEach(this::removeStation);
    }

    private void createFavorite(final String accessToken, final Long source, final Long target) {
        FavoriteRequest request = new FavoriteRequest(source, target);
        // @formatter : off
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                header("Authorization", "Bearer " + accessToken).
                body(request).
        when().
                post("/favorites").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        // @formatter : on
    }

    private List<FavoriteResponse> listFavorites(final String accessToken) {
        // @formatter : off
        return given().
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        header("Authorization", "Bearer " + accessToken).
                when().
                        get("/favorites").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().
                        jsonPath().
                        getList(".", FavoriteResponse.class);
        // @formatter : on
    }

    private void deleteFavorite(final String accessToken, final Long id) {
        // @formatter : off
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                header("Authorization", "Bearer " + accessToken).
        when().
                delete("/favorites/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter : on
    }

}
