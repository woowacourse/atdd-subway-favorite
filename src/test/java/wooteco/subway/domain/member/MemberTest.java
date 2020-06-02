package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
	void update() {
		Member member = new Member("origin@naver.com", "name", "password");
		String updatedName = "updatedName";
		String updatedPassword = "updatedPassword";

		member.update(updatedName, updatedPassword);

		assertThat(member.getName()).isEqualTo(updatedName);
		assertThat(member.getPassword()).isEqualTo(updatedPassword);
	}

}
