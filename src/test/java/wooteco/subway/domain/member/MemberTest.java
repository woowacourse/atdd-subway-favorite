package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
	private Member allen;

	@BeforeEach
	void setUp() {
		allen = new Member("allen@naver.com", "allen", "123");
	}

	@DisplayName("Member의 정보를 갱신한다.")
	@Test
	void update() {
		allen.update("pobi", "456");

		assertThat(allen.getName()).isEqualTo("pobi");
		assertThat(allen.getPassword()).isEqualTo("456");
	}

	// @DisplayName("멤버에 즐겨찾기를 추가한다.")
	// @Test
	// void addFavorite() {
	// 	Favorite favorite = new Favorite(1L, 1L, 5L);
	// 	allen.addFavorite(favorite);
	//
	// 	assertThat(allen.getFavorites()).hasSize(1);
	// }

	@DisplayName("멤버의 비밀번호를 확인한다.")
	@Test
	void checkPassword() {
		assertThat(allen.checkPassword("123")).isTrue();
	}

	// @DisplayName("멤버의 즐겨찾기를 삭제한다.")
	// @Test
	// void deleteFavorite() {
	// 	Favorite favorite = new Favorite(1L, 5L);
	// 	allen.addFavorite(favorite);
	// 	allen.deleteFavorite(1L, 5L);
	//
	// 	assertThat(allen.getFavorites()).hasSize(0);
	// }

	// @DisplayName("멤버의 없는 즐겨찾기를 삭제시 에러가 발생한다.")
	// @Test
	// public void dummy() {
	// 	assertThatThrownBy(() -> allen.deleteFavorite(500L, 1000L))
	// 		.isInstanceOf(FavoriteNotFoundException.class);
	// }
}