package wooteco.subway.exception;

public class InvalidMemberException extends RuntimeException {
	public static final String NOT_FOUND_MEMBER = "해당 사용자를 찾을 수 없습니다 : ";
	public static final String WRONG_PASSWORD = "비밀번호가 틀렸습니다.";
	public static final String DUPLICATED_EMAIL = "이미 가입된 이메일 주소입니다.";
	public static final String FAIL_TO_CREATE = "Member 생성 실패 : ";

	public InvalidMemberException(String message, String email) {
		super(message+ email);
	}
}
