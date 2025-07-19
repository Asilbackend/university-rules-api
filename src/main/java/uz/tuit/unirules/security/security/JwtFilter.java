package uz.tuit.unirules.security.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.handler.ExceptionHandlers;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwUtils;
    private final UserDetailsService userDetailsService;
    //private final Gson gson;

    // private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);

            if (token != null && jwUtils.validateToken(token)) {
                String username = jwUtils.getUsernameFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (!userDetails.isEnabled()) {
                    sendErrorResponse(response, "Sizning akkauntingiz faol emas. Iltimos admin bilan bog`laning");
                    //logger.warn("Foydalanuvchi akkaunti faol emas: {}", username);
                    return;
                }

                var auth = new UsernamePasswordAuthenticationToken(
                        username, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                //logger.info("Logined user: {}", username);
            }
        } catch (Exception ex) {
            //logger.error("Token validatsiya xatosi: {}", ex.getMessage());
            sendErrorResponse(response, "Token noto‘g‘ri yoki muddati o‘tgan");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ExceptionHandlers.ErrorResponse errorResponse = new ExceptionHandlers.ErrorResponse(
                message,
                401, null
        );
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

}
