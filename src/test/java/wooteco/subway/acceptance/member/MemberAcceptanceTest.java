package wooteco.subway.acceptance.member;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.web.member.interceptor.BearerAuthInterceptor.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class MemberAcceptanceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

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
        Response updateResponse = updateMember(tokenResponse, memberResponse,
            "NEW " + TEST_USER_NAME, "NEW " + TEST_USER_PASSWORD);
        MemberResponse persistMember = getMember(tokenResponse);
        //Then : 수정 완료
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(persistMember.getName()).isEqualTo("NEW " + TEST_USER_NAME);

        //When : 회원 탈퇴
        //Then : 탈퇴 완료
        deleteMember(tokenResponse, persistMember);

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
        Response failedUpdateResponse = updateMember(tokenResponse, memberResponse, "", "");
        //Then : 400 에러 발생
        assertThat(failedUpdateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Response createMember(String email, String name, String password) {
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
                extract().response();
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        Response response = given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/login").
            then().
            log().all().
            statusCode(HttpStatus.OK.value())
            .extract()
            .response();
        String token = response.getHeader(HttpHeaders.AUTHORIZATION);
        return TokenResponse.of(token);
    }

    private MemberResponse getMember(TokenResponse tokenResponse) {
        return given()
            .auth()
            .oauth2(tokenResponse.getAccessToken())
            .when()
            .get("/members")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(MemberResponse.class);
    }

    public Response updateMember(TokenResponse tokenResponse, MemberResponse memberResponse,
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
            put("/members/" + memberResponse.getId()).
            then().
            log().all().
            extract().response();
    }

    public void deleteMember(TokenResponse tokenResponse, MemberResponse memberResponse) {
        given().
            auth().
            oauth2(tokenResponse.getAccessToken()).when().
            delete("/members/" + memberResponse.getId()).
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}
