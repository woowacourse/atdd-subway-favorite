package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.acceptance.AcceptanceTest.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@SpringBootTest
@Sql("/truncate.sql")
class FavoriteServiceTest {

	private FavoriteService favoriteService;

	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(memberRepository);
		member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		member.addFavorite(new Favorite(STATION_NAME_YEOKSAM, STATION_NAME_DOGOK));
		member.addFavorite(new Favorite(STATION_NAME_HANTI, STATION_NAME_YANGJAE));
		member.addFavorite(new Favorite(STATION_NAME_DOGOK, STATION_NAME_MAEBONG));
		member = memberRepository.save(member);
	}

	@AfterEach
	void tearDown() {
		memberRepository.deleteAll();
	}

	@Test
	void createFavorite() {
		FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG);
		favoriteService.createFavorite(member, request);
		assertThat(member.getFavorites()).contains(request.toFavorite());
	}

	@Test
	void findFavorites() {
		List<FavoriteResponse> favorites = favoriteService.findFavorites(member);
		assertThat(favorites).hasSize(3);
	}

	@Test
	void deleteFavorite() {
		FavoriteRequest request = new FavoriteRequest(STATION_NAME_YEOKSAM, STATION_NAME_DOGOK);
		favoriteService.deleteFavorite(member, request);
		assertThat(favoriteService.findFavorites(member)).hasSize(2);
	}

	@Test
	void deleteFavorites_DeleteNotExistFavorite() {
		FavoriteRequest request = new FavoriteRequest("", "");
		favoriteService.deleteFavorite(member, request);
		assertThat(favoriteService.findFavorites(member)).hasSize(3);
	}

}
