package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotEmpty();

        MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMember(memberResponse);
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(memberResponse);
    }

    public MemberResponse getMember(String email) {
        // @formatter:off
        return
                given().
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get("/members?email=" + email).
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(MemberResponse.class);
        // @formatter:on
    }

    public void updateMember(MemberResponse memberResponse) {
        UpdateMemberRequest request = new UpdateMemberRequest(
                "NEW_" + TEST_USER_NAME, "NEW_" + TEST_USER_PASSWORD);
        // @formatter:off
        given().
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                put("/members/" + memberResponse.getId()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    public void deleteMember(MemberResponse memberResponse) {
        // @formatter:off
        given().
        when().
                delete("/members/" + memberResponse.getId()).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }
}
