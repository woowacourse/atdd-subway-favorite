package wooteco.subway.web.member.interceptor;

import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

public class InterceptorValidator {
	public boolean isValid(Object handler) {
		NoValidate noValidate = ((HandlerMethod) handler).getMethodAnnotation(NoValidate.class);
		return Objects.isNull(noValidate);
	}
}
