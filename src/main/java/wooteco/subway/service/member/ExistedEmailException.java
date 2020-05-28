package wooteco.subway.service.member;

import wooteco.subway.web.BadRequestForResourcesException;

public class ExistedEmailException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "EXISTED_EMAIL";

    public ExistedEmailException() {
        super(ERROR_CODE, "이미 존재하는 이메일 입니다.");
    }
}
