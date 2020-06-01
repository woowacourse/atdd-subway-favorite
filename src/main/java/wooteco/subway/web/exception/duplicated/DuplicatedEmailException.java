package wooteco.subway.web.exception.duplicated;

public class DuplicatedEmailException extends DuplicatedException {
	public DuplicatedEmailException() {
		super("해당 email을 가진 회원이 이미 존재합니다.");
	}
}
