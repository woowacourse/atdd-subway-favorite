package wooteco.subway.acceptance.member;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.acceptance.Authentication;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @TestFactory
    Stream<DynamicTest> manageMember() {
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
                DynamicTest.dynamicTest("유저 정보 조회", () -> {
                    // when 유저 정보 조회를 한다
                    MemberResponse memberResponse = getMember(TEST_USER_EMAIL, Authentication.of(sessionId, tokenResponse));

                    // then 유저 정보가 조회되었다
                    assertThat(memberResponse.getId()).isEqualTo(Long.parseLong(memberId));
                    assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                    assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
                }),
                DynamicTest.dynamicTest("유저 정보 수정", () -> {
                    // when 유저 정보를 수정한다
                    String newName = "새이름";
                    String newPassword = "새비번";
                    updateMember(memberId, Authentication.of(sessionId, tokenResponse), newName, newPassword);

                    // then 유저 이름이 수정되었다
                    MemberResponse memberResponse = getMember(TEST_USER_EMAIL, Authentication.of(sessionId, tokenResponse));
                    assertThat(memberResponse.getId()).isEqualTo(Long.parseLong(memberId));
                    assertThat(memberResponse.getName()).isEqualTo(newName);

                    // and 유저 비밀번호가 수정되었다
                    Response newResponse = login(TEST_USER_EMAIL, newPassword);
                    assertThat(newResponse).isNotNull();
                }),
                DynamicTest.dynamicTest("유저 정보 삭제", () -> {
                    // when 유저 정보를 삭제한다
                    deleteMember(memberId, Authentication.of(sessionId, tokenResponse));

                    // then 유저 정보가 삭제되었다
                    failToGetMemberByNotExisting(memberId, Authentication.of(sessionId, tokenResponse));
                })
        );
    }
}
