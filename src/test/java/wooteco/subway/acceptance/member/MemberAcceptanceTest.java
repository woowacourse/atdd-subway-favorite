package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.web.member.interceptor.BearerAuthInterceptor.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.Response;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        //When : 회원가입
        Response createResponse = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        //Then : 회원 정보 생성
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.getHeader("Location")).isNotNull();

        //When : 로그인
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        //Then : 토큰 발급
        assertThat(tokenResponse.getTokenType()).isEqualTo(BEARER);
        assertThat(tokenResponse.getAccessToken()).isNotNull();

        //When : 회원 조회
        MemberResponse memberResponse = getMember(tokenResponse);
        //Then : 회원 정보 반환
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        //When : 회원 정보 수정
        Response updateResponse = updateMember(tokenResponse,
            "NEW " + TEST_USER_NAME, "NEW " + TEST_USER_PASSWORD);
        MemberResponse persistMember = getMember(tokenResponse);
        //Then : 수정 완료
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(persistMember.getName()).isEqualTo("NEW " + TEST_USER_NAME);

        //When : 회원 탈퇴
        //Then : 탈퇴 완료
        deleteMember(tokenResponse);

        //When : (예외) 회원가입 시 빈 문자열 입력
        Response failedCreateResponse = createMember("", "", "");
        //Then : 400 에러 발생
        assertThat(failedCreateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(failedCreateResponse.getHeader("Location")).isNull();

        //When : (예외) 회원가입 시 중복된 이메일 입력
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Response failedCreateResponseByDuplicatedEmail = createMember(TEST_USER_EMAIL, "라이언",
            "1234");
        assertThat(failedCreateResponseByDuplicatedEmail.statusCode()).isEqualTo(
            HttpStatus.BAD_REQUEST.value());
        assertThat(failedCreateResponseByDuplicatedEmail.getHeader("Location")).isNull();

        //When : (예외) 회원 정보 수정 시 빈 문자열 입력
        Response failedUpdateResponse = updateMember(tokenResponse,"", "");
        //Then : 400 에러 발생
        assertThat(failedUpdateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public Response updateMember(TokenResponse tokenResponse,
        String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);

        return given().
            auth().
            oauth2(tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            put("/me").
            then().
            log().all().
            extract().response();
    }

    public void deleteMember(TokenResponse tokenResponse) {
        given().
            auth().
            oauth2(tokenResponse.getAccessToken()).when().
            delete("/me").
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}
