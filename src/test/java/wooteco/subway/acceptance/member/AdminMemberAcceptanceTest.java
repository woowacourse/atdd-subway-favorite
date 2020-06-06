package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;

public class AdminMemberAcceptanceTest extends AcceptanceTest {

	@DisplayName("회원 관리 기능 - ADMIN")
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

		assertThat(updatedMember.getName()).isEqualTo(TEST_UPDATE_DELIMITER + TEST_USER_NAME);

		deleteMember(memberResponse);
	}

}
