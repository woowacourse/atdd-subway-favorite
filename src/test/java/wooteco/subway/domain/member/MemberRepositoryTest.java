package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.favorite.Favorite;

@DataJdbcTest
@Sql("/truncate.sql")
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	Member givenMember;
	Favorite givenFavorite1;
	Favorite givenFavorite2;

	@BeforeEach
	void setUp() {
		givenMember = Member.of("kuenhwi@gmail.com", "그니", "1234");
		givenFavorite1 = Favorite.of(1L, 2L).withId(1L);
		givenFavorite2 = Favorite.of(2L, 3L).withId(2L);

		givenMember.addFavorite(givenFavorite1);
		givenMember.addFavorite(givenFavorite2);

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

	@DisplayName("Member에서 Favorite 하나 삭제")
	@Test
	void saveAfterRemove() {
		Member member = memberRepository.findById(1L)
			.orElseThrow(IllegalArgumentException::new);

		member.removeFavorite(givenFavorite2.getSourceId(), givenFavorite2.getTargetId());
		memberRepository.save(member);

		Member saved = memberRepository.findById(1L)
			.orElseThrow(IllegalArgumentException::new);
		assertThat(member).isEqualTo(saved);
	}

	@DisplayName("Member에서 Favorite 하나 추가")
	@Test
	void addFavorite() {
		Member member = memberRepository.findById(1L)
			.orElseThrow(IllegalArgumentException::new);

		Favorite favorite = Favorite.of(3L, 4L);
		member.addFavorite(favorite);
		memberRepository.save(member);

		Member saved = memberRepository.findById(1L)
			.orElseThrow(IllegalArgumentException::new);
		assertThat(saved.getFavorites()).contains(favorite.withId(3L));
	}
}