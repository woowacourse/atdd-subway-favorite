package wooteco.subway.exceptions;

public class DuplicatedEmailException extends RuntimeException {
	public DuplicatedEmailException(String email) {
		super(String.format("%s 로 가입한 회원이 존재합니다.", email));
	}
}
