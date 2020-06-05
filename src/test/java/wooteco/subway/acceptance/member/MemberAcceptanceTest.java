package wooteco.subway.acceptance.member;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 정보 추가, 조회, 수정, 삭제")
    @Test
    void manageMember() {
        // given : 회원 이메일과 이름, 비밀번호가 있다.
        // when : 회원가입을 시도한다.
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // then : 회원가입에 성공한다.
        assertThat(location).isNotBlank();

        // when : 회원이 로그인을 한다.
        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // then : 회원이 로그인을 성공한다.
        MemberResponse memberResponse = getMember(TEST_USER_EMAIL, response.getSessionId(), getResponse(TokenResponse.class, response));
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        // when : 회원 정보를 업데이트한다.
        updateMember(memberResponse, response.getSessionId(), getResponse(TokenResponse.class, response));

        // then : 회원 정보 업데이트가 성공한다.
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL, response.getSessionId(), getResponse(TokenResponse.class, response));
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        // when : 회원 정보를 삭제한다.
        deleteMember(memberResponse, response.getSessionId(), getResponse(TokenResponse.class, response));

        // then : 회원 정보가 삭제되었다.
        Response deletedMemberResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(deletedMemberResponse.getStatusCode()).isEqualTo(404);
    }

    private void updateMember(MemberResponse memberResponse, String sessionId, TokenResponse tokenResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        given().
                cookie("JSESSIONID", sessionId).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("/members/" + memberResponse.getId()).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    private void deleteMember(MemberResponse memberResponse, String sessionId, TokenResponse tokenResponse) {
        given().
                cookie("JSESSIONID", sessionId).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                when().
                delete("/members/" + memberResponse.getId()).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }
}
