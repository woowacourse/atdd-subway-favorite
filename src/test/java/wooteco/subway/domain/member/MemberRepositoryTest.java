package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import wooteco.subway.domain.favorite.Favorite;

@DataJdbcTest
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	Member givenMember;

	@BeforeEach
	void setUp() {
		givenMember = Member.of("kuenhwi@gmail.com", "그니", "1234");
		Favorite favorite1 = new Favorite(1L, 2L);
		Favorite favorite2 = new Favorite(2L, 3L);

		givenMember.addFavorite(favorite1);
		givenMember.addFavorite(favorite2);

		memberRepository.save(givenMember);
	}

	@Test
	void findById() {
		Member member = memberRepository.findById(1L)
			.orElseThrow(IllegalArgumentException::new);
		assertThat(member).isEqualTo(givenMember.withId(1L));
	}

	@Test
	void findByEmail() {
		Member member = memberRepository.findByEmail("kuenhwi@gmail.com")
			.orElseThrow(IllegalArgumentException::new);
		assertThat(member).isEqualTo(givenMember.withId(1L));
	}
}