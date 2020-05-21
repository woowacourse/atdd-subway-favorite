package wooteco.subway.acceptance;

import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.dto.DefaultResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AuthenticationAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인후 멤버 이메일로 멤버 정보 조회")
    @TestFactory
    Stream<DynamicTest> name() {
        return Stream.of(
                dynamicTest("로그인 후 인증정보를 생성", () -> {
                    createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);

                    assertThat(tokenResponse.getTokenType()).isEqualTo("bearer");
                }),
                dynamicTest("로그인 후 이메일로 멤버 정보 조회", () -> {
                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);

                    MemberResponse memberResponse = given()
                            .auth()
                            .oauth2(tokenResponse.getAccessToken())
                            .when()
                            .get("/me")
                            .then()
                            .statusCode(HttpStatus.OK.value())
                            .log().all()
                            .extract().as(new TypeRef<DefaultResponse<MemberResponse>>() {
                            }).getData();

                    assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                })
        );

    }

}
