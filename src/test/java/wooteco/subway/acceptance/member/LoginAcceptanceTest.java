package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AuthAcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AuthAcceptanceTest {

    @DisplayName("로그인 인수 테스트")
    @Test
    void login() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        MemberResponse memberResponse = myInfoWithBearerAuth(tokenResponse);

        updateMember(memberResponse, tokenResponse);
        MemberResponse updatedMemberResponse = myInfoWithBearerAuth(tokenResponse);
        assertThat(updatedMemberResponse.getName()).isEqualTo("NEW_" + TEST_USER_NAME);
        assertThat(updatedMemberResponse.getEmail()).isEqualTo(memberResponse.getEmail());

        deleteMember(updatedMemberResponse, tokenResponse);
        findNotExistMember(updatedMemberResponse, tokenResponse);
    }

    private void findNotExistMember(MemberResponse memberResponse, TokenResponse tokenResponse) {
        given().
            param(memberResponse.getEmail()).
            header("Authorization", "Bearer", tokenResponse.getAccessToken()).
            when()
            .get("/members")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    public void updateMember(MemberResponse memberResponse, TokenResponse tokenResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", TEST_USER_PASSWORD);
        params.put("newPassword", "NEW_" + TEST_USER_PASSWORD);

        given().
            body(params).
            header("Authorization", "Bearer", tokenResponse.getAccessToken()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            put("/members/" + memberResponse.getId()).
            then().
            log().all().
            statusCode(HttpStatus.OK.value());

    }

    public void deleteMember(MemberResponse memberResponse, TokenResponse tokenResponse) {
        given().when().
            header("Authorization", "Bearer", tokenResponse.getAccessToken()).
            delete("/members/" + memberResponse.getId()).
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}
