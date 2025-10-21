package com.animaladoption.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to protect pages that require authentication.
 */
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Remove context path to get the actual path
        String relativePath = path.substring(contextPath.length());

        // Public pages that don't require authentication
        if (isPublicPage(relativePath)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (!isLoggedIn) {
            // Save the requested URL to redirect after login
            httpRequest.getSession(true).setAttribute("redirectAfterLogin", relativePath);

            // Redirect to login page
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // User is authenticated, proceed
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    /**
     * Check if the page is public (doesn't require authentication).
     */
    private boolean isPublicPage(String path) {
        // Normalize path
        path = path.toLowerCase();

        // Public pages and resources
        return path.equals("/") ||
               path.equals("/index.html") ||
               path.equals("/index.jsp") ||
               path.startsWith("/login") ||
               path.startsWith("/register") ||
               path.startsWith("/logout") ||
               path.startsWith("/animals") || // Browse animals is public
               path.startsWith("/animal-detail") || // View animal details is public
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/static/") ||
               path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".gif") ||
               path.endsWith(".ico");
    }
}
