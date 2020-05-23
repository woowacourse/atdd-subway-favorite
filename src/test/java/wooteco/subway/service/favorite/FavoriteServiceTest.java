package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    @Mock
    FavoriteRepository favoriteRepository;

    FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository);
    }

    @DisplayName("즐겨 찾기 생성")
    @Test
    void createFavoriteTest() {
        String source = "강남역";
        String target = "역삼역";
        String memberEmail = "email@gamil.com";
        given(favoriteRepository.save(any())).willReturn(new Favorite(source, target, memberEmail));
        Favorite favorite = favoriteService.createFavorite(source, target, memberEmail);

        assertThat(favorite.getSource()).isEqualTo(source);
        assertThat(favorite.getTarget()).isEqualTo(target);
    }
}
