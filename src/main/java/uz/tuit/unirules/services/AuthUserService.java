package uz.tuit.unirules.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.dto.request_dto.LoginRequestDTO;
import uz.tuit.unirules.dto.respond_dto.LoginRespDto;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.UserRepository;
import uz.tuit.unirules.security.security.JwtUtils;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    @Value("${super.admin.email}")
    String adminEmail;
    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginRespDto login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.login(), loginRequestDTO.password()));
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        System.out.println("getUsername-------------------------------------------------------------------------------------------------");
        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        System.out.println("generateRefreshToken-------------------------------------------------------------------------------------------------");
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        return new LoginRespDto(accessToken, refreshToken);
    }

    public String reLogin(String refreshToken) {
        return jwtUtil.regenerateAccessToken(refreshToken);
    }

    private User getAuthenticatedUser() {
        String username = getUserName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new IllegalArgumentException("principal = null");
        }
        return principal.toString();
    }

    public User getAuthUser() {
        return getAuthenticatedUser();
    }

    public Long getAuthUserId() {
        String userName = getUserName();
        return userRepository.findUserIdByUsername(userName).orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
    }

    public String getAuthUsername() {
        return getUserName();
    }
}
