package Client;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "HttpsFilter", urlPatterns = {"/*"})
public class HttpsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!httpRequest.isSecure() && !httpRequest.getHeader("X-Forwarded-Proto").equals("https")) {
            StringBuilder newUrl = new StringBuilder("https://");
            newUrl.append(httpRequest.getServerName());
            if (httpRequest.getRequestURI() != null) {
                newUrl.append(httpRequest.getRequestURI());
            }
            if (httpRequest.getQueryString() != null) {
                newUrl.append("?").append(httpRequest.getQueryString());
            }
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect(newUrl.toString());
        } else {
            if (chain != null) {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
    }

}
