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
import wooteco.subway.web.error.ErrorResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
            TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        assertThat(createInvalidMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
            TEST_USER_PASSWORD)).isInstanceOf(ErrorResponse.class);

        assertThat(createInvalidMember(TEST_OTHER_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,
            TEST_OTHER_USER_PASSWORD)).isInstanceOf(ErrorResponse.class);


        MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMember(memberResponse);
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(memberResponse);
    }

    private ErrorResponse createInvalidMember(String email, String name, String password,
        String passwordCheck) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);
        params.put("passwordCheck", passwordCheck);

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(ErrorResponse.class);
    }
}
