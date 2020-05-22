package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginMemberAcceptanceTest extends AcceptanceTest {

    /*
    시나리오

    회원을 생성한다.
    제대로 생성됐는지 확인한다.
    생성된 회원으로 로그인한다.
    로그인하고 발급받은 토큰으로 회원정보를 조회한다.
    제대로 조회되는지 확인한다.
    토큰으로 회원정보를 수정한다.
    회원정보가 수정된것을 확인한다.
    발급받은 토큰으로 회원정보를 삭제한다.
    회원이 삭제됐는지 확인한다.
     */

    @Test
    void loginMemberTest() {
        //when 회원을 생성한다.
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        //then 제대로 생성됐는지 확인한다.
        assertThat(location).isNotBlank();

        //when 생성된 회원으로 로그인한다.
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        //when 로그인하고 발급받은 토큰으로 회원정보를 조회한다. (본인의 정보 조회)
        MemberResponse memberResponse = getMember(tokenResponse);
        //then 제대로 조회되는지 확인한다.
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        //when 토큰으로 회원정보를 수정한다. (본인의 정보 수정)
        updateMember(memberResponse);
        //then 회원정보가 수정된것을 확인한다.
        MemberResponse updatedMemberResponse = getMember(tokenResponse);
        assertThat(updatedMemberResponse.getId()).isNotNull();
        assertThat(updatedMemberResponse.getEmail()).isEqualTo("NEW_" + TEST_USER_EMAIL);
        assertThat(updatedMemberResponse.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        //when 발급받은 토큰으로 회원정보를 삭제한다. (본인의 정보 삭제)
        deleteMember(getMember(tokenResponse));
        //then 회원이 삭제됐는지 확인한다.
        verifyEmpty(tokenResponse);
    }
}
