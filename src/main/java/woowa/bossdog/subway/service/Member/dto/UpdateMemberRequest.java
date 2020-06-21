package woowa.bossdog.subway.service.Member.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberRequest {
    private String name;
    private String password;

    public UpdateMemberRequest(final String name, final String password) {
        this.name = name;
        this.password = password;
    }
}
