package uz.tuit.unirules.security.security;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {
    @SneakyThrows
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, x-auth-token");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {
            filterChain.doFilter(request, response);
        } else {
            System.out.println("Pre-flight");
            response.setHeader("Access-Control-Allowed-Methods", "POST, GET, DELETE, PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type,x-auth-token, " +
                    "access-control-request-headers, access-control-request-method, accept, origin, authorization, x-requested-with," +
                    "ngrok-skip-browser-warning");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        doFilterInternal((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

}
