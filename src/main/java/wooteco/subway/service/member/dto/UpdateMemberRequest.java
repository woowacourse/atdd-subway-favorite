package wooteco.subway.service.member.dto;

import wooteco.subway.service.member.vo.UpdateMemberInfo;

public class UpdateMemberRequest {
	private String name;
	private String password;

	public UpdateMemberRequest() {
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

	public UpdateMemberInfo toInfo() {
		return new UpdateMemberInfo(name, password);
	}
}
