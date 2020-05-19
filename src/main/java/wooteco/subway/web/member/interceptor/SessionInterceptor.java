package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = (String)request.getSession().getAttribute("loginMemberEmail");
        if (Strings.isNotBlank(email)) {
            request.setAttribute("loginMemberEmail", email);
            return true;
        }
        return false;
    }
}
