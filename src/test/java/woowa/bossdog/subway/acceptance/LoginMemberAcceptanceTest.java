package woowa.bossdog.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowa.bossdog.subway.service.Member.dto.MemberResponse;
import woowa.bossdog.subway.service.Member.dto.TokenResponse;
import woowa.bossdog.subway.service.Member.dto.UpdateMemberRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginMemberAcceptanceTest extends AcceptanceTest {
    /**
     * 시나리오
     * 1. 회원 가입을 한다.
     * 2. 로그인을 한다.
     * 3. 내 프로필을 수정한다.
     * 4. 회원 탈퇴한다.
     */

    @DisplayName("로그인 회원 괸리")
    @Test
    void manageLoginMember() {
        // 1. 회원 가입을 한다.
        createMember("test@test.com", "bossdog", "test");

        // 2. 로그인 한다.
        TokenResponse tokenResponse = loginMember("test@test.com", "test");
        assertThat(tokenResponse.getTokenType()).isEqualTo("Bearer");
        assertThat(tokenResponse.getAccessToken()).isNotNull();

        // 3. 프로필을 수정한다.
        UpdateMemberRequest updateRequest = new UpdateMemberRequest("changedName", "changedPassword");
        updateLoginMember(tokenResponse.getAccessToken(), updateRequest);

        MemberResponse member = getLoginMember(tokenResponse.getAccessToken());
        assertThat(member.getName()).isEqualTo(updateRequest.getName());
        assertThat(member.getPassword()).isEqualTo(updateRequest.getPassword());

        // 4. 회원 탈퇴한다.
        deleteLoginMember(tokenResponse.getAccessToken());

        List<MemberResponse> members =listMembers();
        assertThat(members).hasSize(0);
    }

    private MemberResponse getLoginMember(final String accessToken) {
        // @formatter : off
        return given().
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        header("Authorization", "Bearer " + accessToken).
                when().
                        get("/me").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(MemberResponse.class);
        // @formatter : on
    }

    private void updateLoginMember(final String accessToken, final UpdateMemberRequest request) {
        // @formatter : off
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                header("Authorization", "Bearer " + accessToken).
                body(request).
        when().
                put("/me").
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
        // @formatter : on
    }

    private void deleteLoginMember(final String accessToken) {
        // @formatter : off
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                header("Authorization", "Bearer " + accessToken).
        when().
                delete("/me").
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter : on
    }
}
