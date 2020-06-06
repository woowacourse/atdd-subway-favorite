package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import wooteco.subway.domain.member.Member;

public class MemberRequest {
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "email은 필수 입력 값입니다.")
	private String email;

	@NotBlank(message = "name은 필수 입력 값입니다.")
	private String name;

	@NotBlank(message = "password는 필수 입력 값입니다.")
	private String password;

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
