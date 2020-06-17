package wooteco.subway.web.exception;

import org.springframework.validation.BindingResult;

public class SameStationException extends RuntimeException {
    private final BindingResult bindingResult;

    public SameStationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
