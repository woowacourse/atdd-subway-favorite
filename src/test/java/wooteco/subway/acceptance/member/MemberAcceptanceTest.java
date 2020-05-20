package wooteco.subway.acceptance.member;

import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.web.dto.DefaultResponse;
import wooteco.subway.web.dto.ErrorCode;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    @DisplayName("중복된 이메일로 회원가입 실패시 Exception 발생")
    @Test
    void createMember() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_USER_EMAIL);
        params.put("name", TEST_USER_NAME);
        params.put("password", TEST_USER_PASSWORD);
        DefaultResponse<Void> defaultResponse = given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(new TypeRef<DefaultResponse<Void>>() {
        });

        assertThat(defaultResponse.getCode()).isEqualTo(ErrorCode.MEMBER_DUPLICATED_EMAIL.getCode());
        assertThat(defaultResponse.getData()).isNull();
        assertThat(defaultResponse.getMessage()).isEqualTo(ErrorCode.MEMBER_DUPLICATED_EMAIL.getMessage());
    }

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
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
