package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

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
    private static final String BASE_PATH = "/members/my-info";

    @DisplayName("내 정보 관리 기능")
    @Test
    void manageMe() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        TokenResponse loginToken = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        MemberResponse memberResponse = getMe(loginToken);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMe(loginToken);
        MemberResponse updatedMember = getMe(loginToken);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMe(loginToken);
    }

    private MemberResponse getMe(TokenResponse loginToken) {
        return
            given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                auth().
                oauth2(loginToken.getAccessToken()).
                when().
                get(BASE_PATH).
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(MemberResponse.class);
    }

    private void updateMe(TokenResponse loginToken) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            auth().
            oauth2(loginToken.getAccessToken()).
            when().
            put(BASE_PATH).
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    private void deleteMe(TokenResponse loginToken) {
        given().
            auth().
            oauth2(loginToken.getAccessToken()).
            when().
            delete(BASE_PATH).
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}
