package woowa.bossdog.subway.service.Member;

import woowa.bossdog.subway.web.advice.exception.BadRequestForResourcesException;

public class ExistedEmailException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "EXISTED_EMAIL";

    public ExistedEmailException() {
        super(ERROR_CODE, "이미 등록된 이메일 입니다.");
    }
}
