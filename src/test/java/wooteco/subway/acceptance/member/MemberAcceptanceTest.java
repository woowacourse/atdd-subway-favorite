package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.dto.ExceptionResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    /**
     * Scenario: 회원 가입을 구현한다.
     * <p>
     * given 이메일, 이름, 비밀번호, 확인용 비밀번호가 주어져 있다.
     * when 회원가입을 요청한다.
     * then 회원이 추가되었다.
     * <p>
     * given 이미 존재하는 이메일과 이름, 비밀번호, 확인용 비밀번호가 주어져 있다.
     * when 회원가입을 요청한다.
     * then 회원으로 추가되지 않았다.
     * <p>
     * given 이메일과 이름, 비밀번호, 확인용 비밀번호 중 정보 한 개가 없다.
     * when 회원가입을 요청한다.
     * then 회원으로 추가되지 않았다.
     * <p>
     * given 비밀번호와 확인용 비밀번호가 다르다.
     * when 회원가입을 요청한다.
     * then 회원으로 추가되지 않았다.
     */

    @DisplayName("회원가입 테스트")
    @Test
    void joinMember() {
        String location = createMemberSuccessfully(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
                TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        ExceptionResponse duplicatedExceptionResponse = createMemberFailed(TEST_USER_EMAIL, "범블비", "bee", "bee",
                HttpStatus.CONFLICT.value());
        assertThat(duplicatedExceptionResponse.getErrorMessage()).isEqualTo(String.format("%s 로 가입한 회원이 존재합니다.",
                TEST_USER_EMAIL));

        ExceptionResponse blankExceptionResponse = createMemberFailed(TEST_USER_EMAIL, "", "bee", "bee",
                HttpStatus.BAD_REQUEST.value());
        assertThat(blankExceptionResponse.getErrorMessage()).isEqualTo("이름은 공란이 될 수 없습니다!");

        ExceptionResponse passwordConflictExceptionResponse = createMemberFailed(TEST_USER_EMAIL, "범블비", "bee", "bee1"
                , HttpStatus.BAD_REQUEST.value());
        assertThat(passwordConflictExceptionResponse.getErrorMessage()).isEqualTo("비밀번호와 비밀번호 확인란에 적은 비밀번호가 다르면 안됩니다!");
    }

    /**
     * Scenario: 로그인 기능을 구현한다.
     * <p>
     * given 회원가입이 되어있는 이메일과 비밀번호가 주어져 있다.
     * when 로그인을 요청한다.
     * then 로그인이 성공한다.
     * <p>
     * given 회원가입이 되어있는 이메일과 유효하지 않은 비밀번호가 주어져 있다.
     * when 로그인을 요청한다.
     * then 로그인이 실패한다.
     * <p>
     * given 회원가입이 되어있지 않은 이메일과 비밀번호가 주어져 있다.
     * when 로그인을 요청한다.
     * then 로그인이 실패한다.
     * <p>
     * given 이메일만 주어져 있다.
     * when 로그인을 요청한다.
     * then 로그인이 실패한다.
     * <p>
     * given 비밀번호만 주어져 있다.
     * when 로그인을 요청한다.
     * then 로그인이 실패한다.
     * <p>
     * given 이메일과 비밀번호가 없다.
     * when 로그인을 요청한다.
     * then 로그인이 실패한다.
     */

    @DisplayName("로그인 테스트")
    @Test
    void loginMember() {
        createMemberSuccessfully(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = loginSuccessfully(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getTokenType()).isEqualTo("bearer");

        ExceptionResponse invalidPasswordResponse = loginFailed(TEST_USER_EMAIL, "INVALID_PASSWORD", HttpStatus.UNAUTHORIZED.value());
        assertThat(invalidPasswordResponse.getErrorMessage()).isEqualTo("비밀번호가 틀렸습니다!");

        ExceptionResponse invalidEmailResponse = loginFailed("NOT_REGISTERED_EMAIL", TEST_USER_PASSWORD, HttpStatus.UNAUTHORIZED.value());
        assertThat(invalidEmailResponse.getErrorMessage()).isEqualTo("NOT_REGISTERED_EMAIL은 가입되지 않은 이메일입니다!");

        ExceptionResponse emptyPasswordResponse = loginFailed(TEST_USER_EMAIL, "", HttpStatus.BAD_REQUEST.value());
        assertThat(emptyPasswordResponse.getErrorMessage()).isEqualTo("비밀번호는 공란이 될 수 없습니다!");

        ExceptionResponse emptyEmailResponse = loginFailed("", TEST_USER_PASSWORD, HttpStatus.BAD_REQUEST.value());
        assertThat(emptyEmailResponse.getErrorMessage()).isEqualTo("이메일은 공란이 될 수 없습니다!");

        ExceptionResponse emptyResponse = loginFailed("", "", HttpStatus.BAD_REQUEST.value());
        assertThat(emptyResponse.getErrorMessage()).isEqualTo("이메일은 공란이 될 수 없습니다!");
    }

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        String location = createMemberSuccessfully(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
                TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMember(memberResponse);
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(memberResponse);
    }
}
