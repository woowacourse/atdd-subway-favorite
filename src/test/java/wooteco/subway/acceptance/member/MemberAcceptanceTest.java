package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.web.exception.ErrorResponse;
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
            DynamicTest.dynamicTest("Create member with not match passwordCheck test", () -> {
                    ErrorResponse error = createMemberWithNotMatchPasswordCheck(TEST_USER_EMAIL,
                        TEST_USER_NAME, TEST_USER_PASSWORD,
                        TEST_OTHER_USER_PASSWORD);
                    assertThat(error.getMessage()).isNotNull();
                }
            ),
            DynamicTest.dynamicTest("Create member with duplicated email", () -> {
                    ErrorResponse error = createMemberWithDuplicatedEmail(TEST_USER_EMAIL,
                        TEST_USER_NAME, TEST_USER_PASSWORD,
                        TEST_USER_PASSWORD);
                    assertThat(error.getMessage()).isNotNull();
                }
            ),
            DynamicTest.dynamicTest("Login test", () -> {
                TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                assertThat(token).extracting(TokenResponse::getAccessToken).isNotNull();
                assertThat(token).extracting(TokenResponse::getTokenType).isEqualTo("bearer");
            }),
            DynamicTest.dynamicTest("Login with not match password", () -> {
                assertThat(loginWithOtherPassword(TEST_USER_EMAIL, TEST_OTHER_USER_PASSWORD))
                    .extracting(ErrorResponse::getMessage).isNotNull();
            }),
            DynamicTest.dynamicTest("Login with not exist email", () -> {
                assertThat(loginWithOtherEmail(TEST_OTHER_USER_EMAIL, TEST_USER_PASSWORD))
                    .extracting(ErrorResponse::getMessage).isNotNull();
            }),
            DynamicTest.dynamicTest("Get member test", () -> {
                MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
                assertThat(memberResponse.getId()).isNotNull();
                assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("Get member with no token", () -> {
                assertThat(myInfoWithNoToken()).extracting(ErrorResponse::getMessage)
                    .isEqualTo("토큰을 찾을 수 없습니다.");
            }),
            DynamicTest.dynamicTest("Update member test", () -> {
                TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
                updateMember(memberResponse, tokenResponse);
                assertThat(getMember(TEST_USER_EMAIL)).extracting(MemberResponse::getName)
                    .isEqualTo("NEW_" + TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("Update member with no token test", () -> {
                MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
                assertThat(updateMemberWithNoToken(memberResponse)).isNotNull();
            }),
            DynamicTest.dynamicTest("Update member with not match password test", () -> {
                TokenResponse tokenResponse = login(TEST_USER_EMAIL, "NEW_" + TEST_USER_PASSWORD);
                MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
                assertThat(
                    updateMemberWithNotMatchPassword(memberResponse, tokenResponse)).isNotNull();
            }),
            DynamicTest.dynamicTest("Delete member with no token test", () -> {
                deleteMemberWithNoToken(getMember(TEST_USER_EMAIL));
            }),
            DynamicTest.dynamicTest("Delete member test", () -> {
                deleteMember(getMember(TEST_USER_EMAIL),
                    login(TEST_USER_EMAIL, "NEW_" + TEST_USER_PASSWORD));
            })
        );
    }
}