package wooteco.subway.web.member.exception;

import wooteco.subway.web.dto.ErrorCode;

public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;

    public MemberException(final String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
