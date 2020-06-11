package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateMemberRequest {

	@NotEmpty
	@NotNull
	private String name;
	@NotEmpty
	@NotNull
	private String password;

	private UpdateMemberRequest() {
	}

	public UpdateMemberRequest(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
}
