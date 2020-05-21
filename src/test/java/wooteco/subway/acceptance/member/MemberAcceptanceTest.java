package wooteco.subway.acceptance.member;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        MemberResponse memberResponse = getMember(tokenResponse);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMember(tokenResponse, memberResponse);
        MemberResponse persistMember = getMember(tokenResponse);
        assertThat(persistMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(tokenResponse, persistMember);
    }

    private String createMember(String email, String name, String password) {
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

    public void updateMember(TokenResponse tokenResponse, MemberResponse memberResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        given().
                auth().
                oauth2(tokenResponse.getAccessToken()).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("/members/" + memberResponse.getId()).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
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
