package wooteco.subway.application.favorite;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.web.member.InvalidAuthenticationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository);
    }

    @Test
    void createFavorite() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 5L);
        Member member = new Member(1L, "email@email.com", "name", "password");
        favoriteService.createFavorite(member, favoriteRequest);

        verify(favoriteRepository).save(any());
    }

    @Test
    void deleteFavorite() {
        Member member = new Member(1L, "email@email.com", "name", "password");
        Favorite favorite = new Favorite(1L, member.getId(), 1L, 5L);
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));

        favoriteService.deleteFavorite(member, favorite.getId());

        verify(favoriteRepository).deleteById(any());
    }

    @Test
    void deleteFavoriteNotMine() {
        Member member = new Member(1L, "email@email.com", "name", "password");
        Favorite favorite = new Favorite(1L, 2L, 1L, 5L);
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));

        assertThrows(InvalidAuthenticationException.class, () -> favoriteService.deleteFavorite(member, favorite.getId()));
    }

    @Test
    void findFavoritesByMember() {
        Member member = new Member(1L, "email@email.com", "name", "password");
        Favorite favorite = new Favorite(1L, 2L, 1L, 5L);
        Favorite favorite2 = new Favorite(2L, 2L, 1L, 3L);
        when(favoriteRepository.findByMemberId(member.getId())).thenReturn(Lists.newArrayList(favorite, favorite2));

        List<Favorite> favorites = favoriteService.findFavoritesByMember(member);

        assertThat(favorites).hasSize(2);
    }
    
}
