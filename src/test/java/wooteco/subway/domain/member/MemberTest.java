package wooteco.subway.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
	public static final String TEST_USER_EMAIL = "brown@email.com";
	public static final String TEST_USER_NAME = "브라운";
	public static final String TEST_USER_PASSWORD = "brown";

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

	@DisplayName("즐겨찾기 경로 추가가 정상적으로 되는지 확인")
	@Test
	void addFavoritePath() {
		Member member = new Member(TEST_USER_EMAIL, "new Brown", TEST_USER_PASSWORD);
		member.addFavoritePath(new FavoritePath(1L, 1L, 2L));
		member.addFavoritePath(new FavoritePath(2L, 3L, 4L));

		assertThat(member.getFavoritePathsIds()).hasSize(2);
	}

	@DisplayName("이미 등록된 즐겨찾기 경로 추가 시 실패하는지 확인")
	@Test
	void failToAddFavoritePathIfAlreadyRegisteredPath() {
		Member member = new Member(TEST_USER_EMAIL, "new Brown", TEST_USER_PASSWORD);
		FavoritePath favoritePath = new FavoritePath(1L, 1L, 2L);
		member.addFavoritePath(favoritePath);

		assertThatThrownBy(() -> member.addFavoritePath(favoritePath))
				.isInstanceOf(DuplicatedFavoritePathException.class)
				.hasMessage("이미 등록된 즐겨찾기 경로입니다!");
	}
}