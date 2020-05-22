package wooteco.subway.web.member.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import wooteco.subway.web.member.InvalidAuthenticationException;

import java.util.Objects;

@Aspect
public class MemberValidateAspect {

	@Pointcut("execution(* wooteco.subway.web.member.MemberController.*(..)) " +
			"&& !@annotation(wooteco.subway.web.member.aspect.NoValidate)")
	// TODO: 2020/05/21 @target이 아니라 @annotation이다.
	// TODO: 2020/05/21 그냥 target은 예약어라 아래 @Around에서 안먹힌다.
	public void valideTarget() {
	}

	@Around("valideTarget()")
	public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		String sessionEmail = (String) args[args.length - 2];
		String jwtEmail = (String) args[args.length - 1];

		if (!Objects.equals(sessionEmail, jwtEmail)) {
			throw new InvalidAuthenticationException("토큰정보와 세션정보가 일치하지 않습니다");
		}

		return joinPoint.proceed();
	}
}
