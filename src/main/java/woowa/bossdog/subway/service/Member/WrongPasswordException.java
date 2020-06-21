package woowa.bossdog.subway.service.Member;

import woowa.bossdog.subway.web.advice.exception.BadRequestForResourcesException;

public class WrongPasswordException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "WRONG_PASSWORD";
    
    public WrongPasswordException() {
        super(ERROR_CODE, "비밀번호가 틀렸습니다.");
    }
}
