package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import wooteco.subway.domain.member.Member;

public class MemberRequest {

	@Email
	@NotNull
	@NotEmpty
	private String email;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@NotEmpty
	private String password;

	private MemberRequest() {
	}

	public MemberRequest(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Member toMember() {
        return new Member(email, name, password);
    }
}
