package woowa.bossdog.subway.service.Member.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.domain.Member;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequest {

    private String email;
    private String name;
    private String password;

    public MemberRequest(final String email, final String name, final String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member toMember() {
        return new Member(email, name, password);
    }
}
