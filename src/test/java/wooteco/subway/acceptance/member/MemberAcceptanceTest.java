package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.CreateMemberException;
import wooteco.subway.service.member.dto.MemberErrorResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입 성공")
    @Test
    void createMemberSucceed() {
        final String TEST_EMAIL = "test@test.com";
        final String TEST_NAME = "testName";
        final String TEST_PASSWORD = "testPassword";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", TEST_EMAIL);
        requestBody.put("name", TEST_NAME);
        requestBody.put("password", TEST_PASSWORD);

        given().
            body(requestBody).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post("/join").
        then().
            log().all().
            statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 존재하는 이메일로 회원가입")
    @Test
    void createMemberWithEmailAlreadyExist() {
        final String TEST_EMAIL = "test@test.com";
        final String TEST_NAME = "testName";
        final String TEST_PASSWORD = "testPassword";
        createMember(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", TEST_EMAIL);
        requestBody.put("name", TEST_NAME);
        requestBody.put("password", TEST_PASSWORD);

        MemberErrorResponse response =
            given().
                body(requestBody).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                post("/join").
            then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(MemberErrorResponse.class);
        assertThat(response.getMessage()).isEqualTo(CreateMemberException.WRONG_CREATE_MESSAGE);
    }

    @DisplayName("요구되는 입력에 빈 값이 있는 경우의 회원가입")
    @Test
    void createMemberWithEmptyInput() {
        final String TEST_EMAIL = "test@test.com";
        final String TEST_NAME = "";
        final String TEST_PASSWORD = "testPassword";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", TEST_EMAIL);
        requestBody.put("name", TEST_NAME);
        requestBody.put("password", TEST_PASSWORD);

        given().
            body(requestBody).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post("/join").
        then().
            log().all().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            extract().as(MemberErrorResponse.class);
    }

    @DisplayName("내 정보 확인에 필요한 내 정보 가져오기")
    @Test
    void manageMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        TokenResponse response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // Read
        MemberResponse memberResponse = getMember(response.getAccessToken(), TEST_USER_EMAIL);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        // Update
        updateMember(response.getAccessToken(), memberResponse);
        MemberResponse updatedMember = getMember(response.getAccessToken(), TEST_USER_EMAIL);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        // Delete
        deleteMember(response.getAccessToken(), memberResponse);
    }

    @DisplayName("로그인 없이 내 정보 가져오기")
    @Test
    //todo: csv로 다양한 케이스
    void retrieveMyInfoWithoutLogin() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        given().
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            get("/members?email=" + TEST_USER_EMAIL).
        then().
            log().all().
            statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("로그인 없이 회원정보 수정하기")
    @Test
    //todo: csv로 다양한 케이스
    void updateMyInfoWithoutLogin() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();
        Map<String, String> params = new HashMap< >();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("email", "NEW_" + TEST_USER_EMAIL);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        given().
            body(params).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            put("/members").
        then().
            log().all().
            statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void deleteMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        TokenResponse response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        given().
            accept(MediaType.APPLICATION_JSON_VALUE).
            header("Authorization", "Bearer " + response.getAccessToken()).
        when().
            delete("/members").
        then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}
