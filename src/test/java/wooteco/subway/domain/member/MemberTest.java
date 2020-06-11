package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.favorite.Favorite;

class MemberTest {

	Member member;

	@BeforeEach
	void setUp() {
		member = Member.of("kuenhwi@gamil.com", "그니", "1234")
			.withId(1L);
	}

	@DisplayName("이름과 패스워드를 변경")
	@Test
	void updateNameAndPassword() {
		Member changed = member.updateNameAndPassword("돼지", "3456");

		assertThat(changed).isEqualTo(
			Member.of("kuenhwi@gamil.com", "돼지", "3456").withId(1L));
	}

	@DisplayName("올바른 비밀번호를 입력하면 통과")
	@Test
	void checkPassword_WhenTrue() {
		boolean isSame = member.checkPassword("1234");

		assertThat(isSame).isTrue();
	}

	@DisplayName("틀린 비밀번호를 입력하면 실패")
	@Test
	void checkPassword_WhenFalse() {
		boolean isSame = member.checkPassword("3456");

		assertThat(isSame).isFalse();
	}

	@DisplayName("즐겨찾기 추가")
	@Test
	void addFavorite() {
		Member added = member.addFavorite(Favorite.of(1L, 2L));
		assertThat(added.getFavorites().getFavorites()).containsExactly(
			Favorite.of(1L, 2L));
	}

	@DisplayName("중복된 즐겨찾기를 추가하면 예외를 발생시킨다.")
	@Test
	void addDuplicatedFavorite() {
		Member given = member.addFavorite(Favorite.of(1L, 2L));

		assertThatThrownBy(() -> given.addFavorite(Favorite.of(1L, 2L)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void removeFavorite() {
		member.addFavorite(Favorite.of(1L, 2L))
			.removeFavorite(1L, 2L);

		assertThat(member.getFavorites().getFavorites()).isEmpty();
	}

	@DisplayName("없는 즐겨찾기 삭제 시도를 하면 예외를 발생시킨다.")
	@Test
	void removeInvalidFavorite() {
		assertThatThrownBy(() -> member.removeFavorite(1L, 2L))
			.isInstanceOf(IllegalArgumentException.class);
	}
}