package wooteco.subway.web.exception.notfound;

public class EmailNotFoundException extends NotFoundException {
	public EmailNotFoundException() {
		super("해당 이메일이 존재하지 않습니다.");
	}
}
