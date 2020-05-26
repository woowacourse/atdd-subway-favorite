package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.web.service.member.dto.MemberResponse;
import wooteco.subway.web.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @TestFactory
    Stream<DynamicTest> manageMember() {
        return Stream.of(
            DynamicTest.dynamicTest("Create member test", () -> {
                    String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
                        TEST_USER_PASSWORD);
                    assertThat(location).isNotBlank();
                }
            ),
            DynamicTest.dynamicTest("Login test", () -> {
                TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                assertThat(token).extracting(TokenResponse::getAccessToken).isNotNull();
                assertThat(token).extracting(TokenResponse::getTokenType).isEqualTo("bearer");
            }),
            DynamicTest.dynamicTest("Get member test", () -> {
                MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
                assertThat(memberResponse.getId()).isNotNull();
                assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("Update member test", () -> {
                TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
                updateMember(memberResponse, tokenResponse);
                assertThat(getMember(TEST_USER_EMAIL)).extracting(MemberResponse::getName)
                    .isEqualTo("NEW_" + TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("Delete member test", () -> {
                deleteMember(getMember(TEST_USER_EMAIL),
                    login(TEST_USER_EMAIL, "NEW_" + TEST_USER_PASSWORD));
            })
        );
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(TokenResponse.class);
    }
}
