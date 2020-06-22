package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberRequest {
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "email은 필수 입력 값입니다.")
	private String email;

	@NotBlank(message = "name은 필수 입력 값입니다.")
	private String name;

	@NotBlank(message = "password는 필수 입력 값입니다.")
	private String password;

	private MemberRequest() {
	}

	public MemberRequest(final String email, final String name, final String password) {
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
		return Member.of(email, name, password);
	}
}
