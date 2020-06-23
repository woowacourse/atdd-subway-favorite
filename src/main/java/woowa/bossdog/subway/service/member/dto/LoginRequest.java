package woowa.bossdog.subway.service.member.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
