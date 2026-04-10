package com.electromart.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
// bổ sung thêm đường dẫn cần auth 
@WebFilter(urlPatterns = {"/admin/*", "/receiving"})
public class AdminAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Boolean isAdmin = (Boolean) req.getSession().getAttribute("IS_ADMIN");
        if (Boolean.TRUE.equals(isAdmin)) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/admin");
        }
    }
}
