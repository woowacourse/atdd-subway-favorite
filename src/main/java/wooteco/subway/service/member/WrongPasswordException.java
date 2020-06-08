package wooteco.subway.service.member;

import wooteco.subway.web.advice.exception.BadRequestForResourcesException;

public class WrongPasswordException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "WRONG_PASSWORD";

    public WrongPasswordException() {
        super(ERROR_CODE, "잘못된 패스워드입니다. 다시 확인해주세요");
    }
}
