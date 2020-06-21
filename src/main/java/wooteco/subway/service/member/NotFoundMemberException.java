package wooteco.subway.service.member;

/**
 * class description
 *
 * @author HyungJu An, MinWoo Yim
 */
public class NotFoundMemberException extends MemberException {
	public NotFoundMemberException(final String message) {
		super(message);
	}
}
