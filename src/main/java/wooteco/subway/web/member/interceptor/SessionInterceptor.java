package wooteco.subway.web.member.interceptor;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = (String) request.getSession().getAttribute("loginMemberEmail");
        if (Strings.isNotBlank(email)) {
            request.setAttribute("loginMemberEmailSession", email);
        }

        return true;
    }
}
