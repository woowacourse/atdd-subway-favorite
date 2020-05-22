package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

	@DisplayName("회원 관리 기능")
	@Test
	void manageMember() {
		// given : 회원이 등록되어 있다.
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		// and : 로그인이 되어 있다.
		TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		// when : 회원정보를 조회한다.
		MemberResponse memberResponse = getMember(token);

		// then : 회원정보를 응답받는다.
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);

		// when : 회원정보를 수정한다.

		updateMember(token, NEW_TEST_USER_NAME, TEST_USER_PASSWORD);

		// then : 회원정보가 수정되었다.
		MemberResponse updatedMember = getMember(token);
		assertThat(updatedMember.getName()).isEqualTo(NEW_TEST_USER_NAME);

		// when : 회원정보를 삭제한다.
		deleteMember(token);

		// then : 회원정보가 삭제되었다.
		assertThatThrownBy(() -> getMember(token))
			.isInstanceOf(AssertionError.class);
	}
}
