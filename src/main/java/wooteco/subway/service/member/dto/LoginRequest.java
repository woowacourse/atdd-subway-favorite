package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "email은 필수 입력 값입니다.")
	private String email;

	@NotBlank(message = "password은 필수 입력 값입니다.")
	private String password;

	private LoginRequest() {
	}

	public LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
