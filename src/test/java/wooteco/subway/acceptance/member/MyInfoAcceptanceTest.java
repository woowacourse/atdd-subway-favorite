package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.AuthAcceptanceTest.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MyInfoAcceptanceTest extends AcceptanceTest {

    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "1234";
    public static final String TEST_WRONG_PASSWORD = "1111";
    public static final String ME_URL = "/me";

    public static String createMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().header("Location");
    }

    public static void deleteMemberWithBearerAuth(TokenResponse tokenResponse) {
        given().auth().
            oauth2(tokenResponse.getAccessToken()).
            when().
            delete(ME_URL).
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void deleteMemberWithoutBearerAuth() {
        given().when().
            delete(ME_URL).
            then().
            log().all().
            statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void updateMemberWithBearerAuth(TokenResponse tokenResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        given().auth().
            oauth2(tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            put(ME_URL).
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    public static void updateMemberWithoutBearerAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            put(ME_URL).
            then().
            log().all().
            statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("사용자 실패 CRUD 및 성공 CRUD")
    @Test
    void manageMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        failLogin(TEST_USER_EMAIL, TEST_WRONG_PASSWORD);

        myInfoWithoutBearerAuth();
        updateMemberWithoutBearerAuth();
        deleteMemberWithoutBearerAuth();

        TokenResponse tokenResponse = successLogin(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        MemberResponse memberResponse = myInfoWithBearerAuth(tokenResponse);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMemberWithBearerAuth(tokenResponse);
        MemberResponse updatedMemberResponse = myInfoWithBearerAuth(tokenResponse);
        assertThat(updatedMemberResponse.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMemberWithBearerAuth(tokenResponse);
    }
}
