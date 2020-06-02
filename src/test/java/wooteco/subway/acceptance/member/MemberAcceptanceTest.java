package wooteco.subway.acceptance.member;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.acceptance.Authentication;
import wooteco.subway.service.member.dto.MemberResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @TestFactory
    Stream<DynamicTest> manageMember() {
        // given 회원 가입 된 유저가 있다
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        String memberId = location.split("/")[2];

        // 회원 가입이 잘 되어 있다
        assertThat(memberId).isNotEmpty();

        // and 로그인을 했다
        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        Authentication authentication = Authentication.of(response.getSessionId(), getTokenResponse(response));

        // 로그인이 잘 되어 있다
        assertThat(response).isNotNull();

        return Stream.of(
                DynamicTest.dynamicTest("유저 정보 조회", () -> {
                    // when 유저 정보 조회를 한다
                    MemberResponse memberResponse = getMember(TEST_USER_EMAIL, authentication);

                    // then 유저 정보가 조회되었다
                    assertThat(memberResponse.getId()).isEqualTo(Long.parseLong(memberId));
                    assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                    assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
                }),
                DynamicTest.dynamicTest("유저 정보 조회 실패(잘못된 이메일)", () -> {
                    // when 잘못된 이메일로 유저 정보 조회를 한다
                    // then 유저 정보 조회가 실패한다
                    failToGetMemberByNotExisting(TEST_USER_EMAIL + "dummy", authentication);
                }),
                DynamicTest.dynamicTest("유저 정보 수정", () -> {
                    // when 유저 정보를 수정한다
                    String newName = "새이름";
                    String newPassword = "새비번";
                    updateMember(memberId, authentication, newName, newPassword);

                    // then 유저 이름이 수정되었다
                    MemberResponse memberResponse = getMember(TEST_USER_EMAIL, authentication);
                    assertThat(memberResponse.getId()).isEqualTo(Long.parseLong(memberId));
                    assertThat(memberResponse.getName()).isEqualTo(newName);

                    // and 유저 비밀번호가 수정되었다
                    Response newResponse = login(TEST_USER_EMAIL, newPassword);
                    assertThat(newResponse).isNotNull();
                }),
                DynamicTest.dynamicTest("유저 정보 삭제", () -> {
                    // when 유저 정보를 삭제한다
                    deleteMember(memberId, authentication);

                    // then 유저 정보가 삭제되었다
                    failToGetMemberByNotExisting(memberId, authentication);
                })
        );
    }
}
