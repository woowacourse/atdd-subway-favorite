package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class MemberAcceptanceTest extends AcceptanceTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("회원 관리 기능")
    @Test
    void manageMember() {
        String token = jwtTokenProvider.createToken(TEST_USER_EMAIL);
        String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD,token);
        assertThat(location).isNotBlank();

        MemberResponse memberResponse = getMember(TEST_USER_EMAIL);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

        updateMember(token);
        MemberResponse updatedMember = getMember(TEST_USER_EMAIL);
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(token);
    }
}
