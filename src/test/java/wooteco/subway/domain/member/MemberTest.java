package wooteco.subway.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.DuplicatedFavoriteException;
import wooteco.subway.exception.InvalidAuthenticationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberTest {

	@DisplayName("입력받은 id가 member와 일치하지 않을 경우 예외처리")
	@Test
	void validateId() {
		Long id = 1L;
		Member member = new Member(2L, "toney@gmail.com", "toney", "password");
		assertThatThrownBy(() -> member.validateId(id))
				.isInstanceOf(InvalidAuthenticationException.class)
				.hasMessage("잘못된 로그인이에요.");
	}

	@DisplayName("입력받은 즐겨찾기가 이미 있는 경우 예외처리")
	@Test
	void validateDuplicatedFavorite() {
		Member member = new Member(1L, "toney@gmail.com", "toney", "password");
		member.addFavorite(Favorite.of(1L, 2L));
		assertThatThrownBy(() -> member.validateDuplicatedFavorite(1L, 2L))
				.isInstanceOf(DuplicatedFavoriteException.class)
				.hasMessage("이미 추가된 즐겨찾기에요.");
	}
}
