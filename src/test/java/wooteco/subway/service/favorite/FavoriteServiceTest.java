package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

	private FavoriteService favoriteService;

	@Mock
	private FavoriteRepository favoriteRepository;

	@BeforeEach
	public void setUp() {
		favoriteService = new FavoriteService(favoriteRepository);
	}

	@Test
	public void createFavoriteTest() {
		Favorite favorite = new Favorite(1L, "", "");
		when(favoriteRepository.save(any())).thenReturn(favorite);
		Favorite persistFavorite = favoriteService.createFavorite(favorite);

		assertThat(persistFavorite).isEqualTo(favorite);
	}
}