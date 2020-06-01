package wooteco.subway.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.AcceptanceTest.*;

class MemberTest {

	@DisplayName("이름만 갱신 시 반영되는지 확인")
	@Test
	void updateName() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Member expectedMember = new Member(TEST_USER_EMAIL, "new Brown", TEST_USER_PASSWORD);
		member.update("new Brown", "");

		assertThat(member).isEqualTo(expectedMember);
	}

	@DisplayName("비밀번호만 갱신 시 반영되는지 확인")
	@Test
	void updatePassword() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Member expectedMember = new Member(TEST_USER_EMAIL, TEST_USER_NAME, "new Password");
		member.update("", "new Password");

		assertThat(member).isEqualTo(expectedMember);
	}

	@DisplayName("이름과 비밀번호 갱신 시 반영되는지 확인")
	@Test
	void updateNameAndPassword() {
		Member member = new Member(TEST_USER_EMAIL, "new Brown", TEST_USER_PASSWORD);
		Member expectedMember = new Member(TEST_USER_EMAIL, "new Brown", "new Password");
		member.update("new Brown", "new Password");

		assertThat(member).isEqualTo(expectedMember);
	}
}