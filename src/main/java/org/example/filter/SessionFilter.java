package org.example.filter;

import org.example.redis.RedisManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@WebFilter("/*")
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * 1. updates session data in redis.
     * 2. creates http session from redis when request comes with session id
     * which doesn't have associated http session but has associated redis session.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        interceptRequest(request);
        filterChain.doFilter(request, response);
        updateSessionDataInRedisAfterResponseIsSentToClient(request);
    }

    @Override
    public void destroy() {}

    private void interceptRequest(HttpServletRequest request) {
        boolean sessionNotExists = request.getSession(false) == null;
        String requestedSessionId = request.getRequestedSessionId();
        boolean requestedSessionIdExists = requestedSessionId != null;
        // session timeout relies on redis expiry, not on web.xml config
        boolean eligibleToCreateHttpSessionFromRedis =
                sessionNotExists && requestedSessionIdExists && RedisManager.sessionExists(requestedSessionId);
        if (eligibleToCreateHttpSessionFromRedis) {
            createHttpSessionFromRedis(request, requestedSessionId);
        }
    }

    // TODO should implement mapping from json to pojo
    private void createHttpSessionFromRedis(HttpServletRequest request, String requestedSessionId) {
        HttpSession replicatedSession = request.getSession(true);
        Map<String, String> sessionData = RedisManager.getSessionAttributes(requestedSessionId);
        sessionData.forEach(replicatedSession::setAttribute);
        System.out.println("created http session from redis");
    }

    // TODO should implement mapping from pojo to json
    private void updateSessionDataInRedisAfterResponseIsSentToClient(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionId = session.getId();
            for (Enumeration<String> enumeration = session.getAttributeNames(); enumeration.hasMoreElements(); ) {
                String attributeName = enumeration.nextElement();
                RedisManager
                        .setSessionAttribute(sessionId, attributeName, (String) session.getAttribute(attributeName));
            }
            System.out.println("updated session data in redis");
        }
    }

}
