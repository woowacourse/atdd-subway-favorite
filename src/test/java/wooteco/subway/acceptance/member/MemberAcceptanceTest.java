package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    //        Feature: 회원관리
    //
    //        Scenario: 회원을 관리한다.
    //        When 회원 가입 요청을 한다.
    //        Then 회원으로 가입이되었다.
    //
    //        When 로그인을 한다.
    //        Then 로그인이 되었다.
    //
    //        When 로그인 후 회원정보를 조회한다.
    //        Then 회원정보를 받아온다.
    //
    //        When 로그인 후 회원정보 수정 요청을 한다.
    //        Then 회원정보가 수정되었다.
    //
    //        When 로그인 후 회원정보 삭제 요청을 한다.
    //        Then 회원정보가 삭제되었다.
    //
    //        When 삭제된 아이디로 로그인 시도를 한다.
    //		  Then 로그인이 되지 않는다.

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME,
                TEST_USER_PASSWORD);
        assertThat(location).matches("^/members/[1-9][0-9]*$");

        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getTokenType()).isEqualTo("bearer");

        MemberResponse memberResponse = getMember(TEST_USER_EMAIL, tokenResponse);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMember(memberResponse, tokenResponse);
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL, tokenResponse);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(memberResponse, tokenResponse);

        loginForNotExistMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }
}
