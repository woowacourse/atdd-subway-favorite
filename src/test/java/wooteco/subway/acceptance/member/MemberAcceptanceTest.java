package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {
    /*
     * when 회원가입 요청을 한다.
     * and 로그인 요청을 한다.
     * then 회원 조회가 가능하다.
     *
     * when 회원 정보를 수정한다.
     * then 회원 정보를 조회해서 수정 잘됐는지 확인한다.
     *
     * when 회원 탈퇴 한다.
     * then 로그인 요청을 해서 탈퇴되었는지 확인한다.
     * */

    @DisplayName("회원 자기 자신이 정보를 관리한다")
    @Test
    void manageMemberSelf() {
        String member = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(member).isNotBlank();

        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        MemberResponse memberResponse = myInfoWithBearerAuth(tokenResponse);

        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);

        updateInfoBearerAuth(tokenResponse);

        memberResponse = myInfoWithBearerAuth(tokenResponse);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMemberWithBearerAuth(tokenResponse);

        badLogin(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    /*
    given 로그인 하지 않은 상태
    when 회원정보를 조회한다.
    then 회원정보 조회가 불가능하다.

    when 회원정보를 수정한다.
    then 회원정보 수정이 불가능하다.

    when 회원정보를 삭제한다.
    then 회원정보 삭제가 불가능하다.
    */
    @DisplayName("로그인 하지않고 회원정보를 관리하려 하는 경우")
    @Test
    public void manageMemberWithoutLogin() {
        getMemberWithoutLogin();
        updateMemberWithoutLogin();
        deleteMemberWithoutLogin();
    }

    @DisplayName("잘못된 형식으로 회원가입 할경우")
    @Test
    public void NotCreateMember() {
        notCreateMember("123", "a", "123");
        notCreateMember("123@a.com", "", "123");
        notCreateMember("123@a.com", "a", "");
    }

    public void notCreateMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/members").
            then().
            log().all().
            statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
