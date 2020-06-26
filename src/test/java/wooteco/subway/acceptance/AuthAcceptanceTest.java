package wooteco.subway.acceptance;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Session Bearer 둘다")
    @TestFactory
    Stream<DynamicTest> AuthorizeSessionAndBearer() {
        // given 회원 가입 된 유저가 두 명 있다
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        String location2 = createMember(TEST_USER_EMAIL2, TEST_USER_NAME2, TEST_USER_PASSWORD2);
        String memberId = location.split("/")[2];
        String memberId2 = location2.split("/")[2];

        // 회원 가입이 잘 되어 있다
        assertThat(memberId).isNotEmpty();
        assertThat(memberId2).isNotEmpty();

        // and 둘 다 로그인을 했다
        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        Response response2 = login(TEST_USER_EMAIL2, TEST_USER_PASSWORD2);
        String sessionId = response.getSessionId();
        String sessionId2 = response2.getSessionId();
        TokenResponse tokenResponse = getTokenResponse(response);
        TokenResponse tokenResponse2 = getTokenResponse(response2);

        // 로그인이 잘 되어 있다
        assertThat(response).isNotNull();
        assertThat(response2).isNotNull();

        return Stream.of(
                DynamicTest.dynamicTest("로그인 실패(잘못된 비밀번호)", () -> {
                    // when 잘못된 비밀번호로 로그인을 시도한다
                    Response badRequestResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD2);

                    // then 로그인이 실패했다
                    assertThat(badRequestResponse.statusCode()).isEqualTo(401);
                }),
                DynamicTest.dynamicTest("유저 인가 실패(토큰 존재 x)", () -> {
                    // when 토큰 없이 유저 정보를 조회한다
                    // then 유저 정보 조회가 실패했다
                    failToGetMemberByAuthentication(TEST_USER_EMAIL, Authentication.of(sessionId, new TokenResponse("", "")));
                }),
                DynamicTest.dynamicTest("유저 인가 실패(잘못된 토큰)", () -> {
                    // when 다른 유저의 토큰으로 정보 조회를 한다
                    // then 유저 정보 조회가 실패했다
                    failToGetMemberByAuthentication(TEST_USER_EMAIL, Authentication.of(sessionId, tokenResponse2));
                }),
                DynamicTest.dynamicTest("유저 인가 실패(세션 존재 x)", () -> {
                    // when 세션 없이 유저 정보를 조회한다
                    // then 유저 정보 조회가 실패했다
                    failToGetMemberByAuthentication(TEST_USER_EMAIL, Authentication.of("", tokenResponse));
                }),
                DynamicTest.dynamicTest("유저 인가 실패(잘못된 세션)", () -> {
                    // when 다른 유저의 세션Id로 정보 조회를 한다
                    // then 유저 정보 조회가 실패했다
                    failToGetMemberByAuthentication(TEST_USER_EMAIL, Authentication.of(sessionId2, tokenResponse));
                }),
                DynamicTest.dynamicTest("유저 인가 실패(잘못된 id)", () -> {
                    // when 나의 세션, 토큰을 가지고 다른 유저의 id로 정보 조회를 한다
                    // then 유저 정보 조회가 실패했다
                    failToGetMemberByIdBecauseOfAuthentication(memberId2, Authentication.of(sessionId, tokenResponse));
                }),
                DynamicTest.dynamicTest("유저 인가 성공", () -> {
                    // when 자기 세션 Id와 자기 토큰 정보로 조회를 한다
                    MemberResponse memberResponse = getMember(TEST_USER_EMAIL, Authentication.of(sessionId, tokenResponse));

                    // then 자기 정보가 조회되었다
                    assertThat(memberResponse.getId()).isEqualTo(Long.parseLong(memberId));
                    assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                    assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
                })
        );
    }
}
