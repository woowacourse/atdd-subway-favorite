package wooteco.subway.web.member;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {
    private final MemberService memberService;

    public FavoriteController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
        @LoginMember Member member,
        @RequestBody FavoriteRequest request
    ) {
        memberService.addFavorite(member, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getAll(
        @LoginMember Member member
    ) {
        List<FavoriteResponse> response = memberService.getAllFavorites(member);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(
        @LoginMember Member member,
        @PathVariable Long id
    ) {
        memberService.removeFavoriteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
