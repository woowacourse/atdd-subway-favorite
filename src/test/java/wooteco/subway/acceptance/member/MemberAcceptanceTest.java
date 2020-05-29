package wooteco.subway.acceptance.member;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.exception.NoMemberExistException;
import wooteco.subway.service.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        // given 회원가입된 유저가 있다
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(location).isNotBlank();

        // and 유저가 로그인을 했다
        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        //when 유저 정보 조회를 한다
        MemberResponse memberResponse = getMember(TEST_USER_EMAIL, response.getSessionId(), getTokenResponse(response));

        //then 유저 정보가 조회되었다
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        //when 유저 정보를 업데이트한다
        updateMember(memberResponse, response.getSessionId(), getTokenResponse(response));

        //then 유저 정보가 업데이트 되었다
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL, response.getSessionId(), getTokenResponse(response));
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        //when 유저 정보를 삭제한다
        deleteMember(memberResponse, response.getSessionId(), getTokenResponse(response));

        //then 유저 정보가 삭제되었다
        failToGetMember(TEST_USER_EMAIL);
    }
}
