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

	@DisplayName("멤버의 비밀번호를 확인한다.")
	@Test
	void checkPassword() {
		assertThat(allen.checkPassword("123")).isTrue();
	}
}