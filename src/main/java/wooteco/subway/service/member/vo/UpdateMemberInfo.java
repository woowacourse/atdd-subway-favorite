package wooteco.subway.service.member.vo;

public class UpdateMemberInfo {
	private String name;
	private String password;

	public UpdateMemberInfo(String name, String password) {
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
