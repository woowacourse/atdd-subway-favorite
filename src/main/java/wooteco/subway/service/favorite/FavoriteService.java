package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;

import java.util.List;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void deleteFavoriteByStationId(Long id) {
        List<Member> members = memberRepository.findAll();
        members.forEach(member -> member.removeFavoriteByStationId(id));
        memberRepository.saveAll(members);
    }
}
