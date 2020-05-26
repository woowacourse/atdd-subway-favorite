package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@AutoConfigureMockMvc
public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() throws Exception {
        // given 회원이 존재하고 로그인이 되어있다.
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        String location2 = createMember(TEST_USER_EMAIL2, TEST_USER_NAME2, TEST_USER_PASSWORD2);
        assertThat(location).isNotBlank();
        assertThat(location2).isNotBlank();
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        TokenResponse token2 = login(TEST_USER_EMAIL2, TEST_USER_PASSWORD2);

        // when 회원정보 조회 요청을 보낸다.
        MemberResponse member = getMember(TEST_USER_EMAIL, token);
        // then 회원정보가 조회된다.
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(member.getName()).isEqualTo(TEST_USER_NAME);

        // when 다른 사용자로 회원 정보 조회 요청을 보낸다.
        MemberResponse member2 = getMember(TEST_USER_EMAIL2, token2);
        // then 다른 사용자의 회원정보가 조회된다.
        assertThat(member2.getId()).isNotNull();
        assertThat(member2.getEmail()).isEqualTo(TEST_USER_EMAIL2);
        assertThat(member2.getName()).isEqualTo(TEST_USER_NAME2);

        // when 인증 없이 회원 정보 수정 요청을 보낸다.
        UpdateMemberRequest updateDto = new UpdateMemberRequest("Hodol", "1234");
        updateMemberWithoutAuthentication(member, updateDto);
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL, token);
        // then 회원 정보는 수정되지 않는다.
        assertThat(updatedMember.getName()).isEqualTo(TEST_USER_NAME);

        // when 다른 사용자 인증과 함께 회원 정보 수정 요청을 보낸다.
        updateMemberWithAuthentication(member, token2, updateDto);
        // then 회원 정보가 수정되었다.
        MemberResponse shouldNotBeUpdated = getMember(TEST_USER_EMAIL, token);
        assertThat(shouldNotBeUpdated.getName()).isEqualTo(TEST_USER_NAME);

        // when 인증과 함께 회원 정보 수정 요청을 보낸다.
        updateMemberWithAuthentication(member, token, updateDto);
        // then 회원 정보가 수정되었다.
        MemberResponse updateMember = getMember(TEST_USER_EMAIL, token);
        assertThat(updateMember.getName()).isEqualTo(updateDto.getName());
        // and 기존의 비밀번호로는 로그인할 수 없다.
        assertThatThrownBy(() -> login(TEST_USER_EMAIL, TEST_USER_PASSWORD))
            .isInstanceOf(Exception.class);

        // when 다른 사용자 인증과 함께 회원 정보 삭제 요청을 보낸다.
        deleteMember(member, token2);
        // then 회원정보가 삭제되지 않는다.
        assertThat(getMember(TEST_USER_EMAIL, token).getName()).isNotNull();

        // when 사용자 인증과 함께 회원 정보 삭제 요청을 보낸다.
        deleteMember(member, token);
        // then 회원정보가 삭제된다.
        assertThat(getMember(TEST_USER_EMAIL, token)).isNull();
    }
}
