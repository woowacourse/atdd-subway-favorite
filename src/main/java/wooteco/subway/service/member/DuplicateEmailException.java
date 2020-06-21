package wooteco.subway.service.member;

/**
 * 중복 이메일 예외 클래스입니다.
 *
 * @author HyungJu An, MinWoo Yim
 */
public class DuplicateEmailException extends MemberException {
	public DuplicateEmailException(final String message) {
		super(message);
	}
}
