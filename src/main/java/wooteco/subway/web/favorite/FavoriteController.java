package wooteco.subway.web.favorite;

import java.net.URI;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteDetailResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.web.member.interceptor.IsAuth;
import wooteco.subway.web.member.resolver.LoginMember;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @IsAuth
    @GetMapping("/me")
    public ResponseEntity<Set<FavoriteDetailResponse>> getMemberFavorites(@LoginMember Member member) {
        Set<FavoriteDetailResponse> responses = favoriteService.getAll(member);
        return ResponseEntity.ok().body(responses);
    }

    @IsAuth
    @PostMapping
    public ResponseEntity<Void> create(@LoginMember Member member,
        @RequestBody FavoriteRequest favoriteRequest) {
        Member persistMember = favoriteService.addToMember(member, favoriteRequest);
        return ResponseEntity.created(URI.create("/members/" + persistMember.getId() + "/favorites"))
            .build();
    }

    @IsAuth
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember Member member, @PathVariable Long id) {
        favoriteService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
