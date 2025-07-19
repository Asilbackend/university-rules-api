package uz.tuit.unirules.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.request_dto.LoginRequestDTO;
import uz.tuit.unirules.dto.respond_dto.LoginRespDto;
import uz.tuit.unirules.services.AuthUserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthUserService authUserService;

    public AuthController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    public HttpEntity<LoginRespDto> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        LoginRespDto login = authUserService.login(loginRequestDTO);
        return ResponseEntity.ok(login);
    }


    @PostMapping("/refresh-token")
    public Map<String, String> getNewAccessToken(@RequestParam String refreshToken) {
        String accessToken = authUserService.reLogin(refreshToken);
        return Map.of("accessToken", accessToken);
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout qiling";
    }
}
