package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@AutoConfigureMockMvc
public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @TestFactory
    Stream<DynamicTest> manageMember() throws Exception {
        // given
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        String location2 = createMember(TEST_USER_EMAIL2, TEST_USER_NAME2, TEST_USER_PASSWORD2);
        assertThat(location).isNotBlank();
        assertThat(location2).isNotBlank();
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        TokenResponse token2 = login(TEST_USER_EMAIL2, TEST_USER_PASSWORD2);

        return Stream.of(
            DynamicTest.dynamicTest("회원 정보 조회", () -> {
                // when
                MemberResponse member = getMember(TEST_USER_EMAIL, token);
                // then
                assertThat(member.getId()).isNotNull();
                assertThat(member.getEmail()).isEqualTo(TEST_USER_EMAIL);
                assertThat(member.getName()).isEqualTo(TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("다른 사용자로 회원 정보 조회", () -> {
                // when
                MemberResponse member2 = getMember(TEST_USER_EMAIL2, token2);
                // then
                assertThat(member2.getId()).isNotNull();
                assertThat(member2.getEmail()).isEqualTo(TEST_USER_EMAIL2);
                assertThat(member2.getName()).isEqualTo(TEST_USER_NAME2);
            }),
            DynamicTest.dynamicTest("인증 없이 회원 정보 수정", () -> {
                // given
                MemberResponse member = getMember(TEST_USER_EMAIL, token);
                UpdateMemberRequest updateDto = new UpdateMemberRequest("Hodol", "1234");
                // when
                updateMemberWithoutAuthentication(member, updateDto);
                MemberResponse updatedMember = getMember(TEST_USER_EMAIL, token);
                // then
                assertThat(updatedMember.getName()).isEqualTo(TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("다른 사용자 인증으로 회원 정보 수정", () -> {
                // given
                MemberResponse member = getMember(TEST_USER_EMAIL, token);
                UpdateMemberRequest updateDto = new UpdateMemberRequest("Hodol", "1234");
                // when
                updateMemberWithAuthentication(member, token2, updateDto);
                // then
                MemberResponse shouldNotBeUpdated = getMember(TEST_USER_EMAIL, token);
                assertThat(shouldNotBeUpdated.getName()).isEqualTo(TEST_USER_NAME);
            }),
            DynamicTest.dynamicTest("인증과 함께 회원 정보 수정", () -> {
                // given
                MemberResponse member = getMember(TEST_USER_EMAIL, token);
                UpdateMemberRequest updateDto = new UpdateMemberRequest("Hodol", "1234");
                // when
                updateMemberWithAuthentication(member, token, updateDto);
                // then
                MemberResponse updateMember = getMember(TEST_USER_EMAIL, token);
                assertThat(updateMember.getName()).isEqualTo(updateDto.getName());
                // and 기존의 비밀번호로는 로그인할 수 없다.
                assertThat(loginError(TEST_USER_EMAIL, TEST_USER_PASSWORD).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
            }),
            DynamicTest.dynamicTest("다른 사용자 인증으로 회원 정보 삭제", () -> {
                // given
                MemberResponse member = getMember(TEST_USER_EMAIL, token);
                // when
                deleteMember(member, token2);
                // then
                assertThat(getMember(TEST_USER_EMAIL, token).getName()).isNotNull();
            }),
            DynamicTest.dynamicTest("사용자 인증과 함께 회원 정보 삭제", () -> {
                // given
                MemberResponse member = getMember(TEST_USER_EMAIL, token);
                // when
                deleteMember(member, token);
                // then
                assertThat(getMemberError(TEST_USER_EMAIL, token).getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND.value());

            })
        );
    }
}
