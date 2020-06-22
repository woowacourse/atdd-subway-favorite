package woowa.bossdog.subway.service.member.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String password;

    public MemberResponse(final Long id, final String email, final String name, final String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(), member.getPassword());
    }

    public static List<MemberResponse> listFrom(final List<Member> members) {
        return members.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }
}
