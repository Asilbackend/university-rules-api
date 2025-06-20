package uz.tuit.unirules.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.request_dto.LoginRequestDTO;
import uz.tuit.unirules.dto.respond_dto.LoginRespDto;
import uz.tuit.unirules.services.AuthUserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthUserService authUserService;

    public AuthController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    public LoginRespDto login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        return authUserService.login(loginRequestDTO);
    }

    @PostMapping("/refresh-login")
    public String getNewAccessToken(@RequestParam String refreshToken) {
        return authUserService.reLogin(refreshToken);
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout qiling";
    }
}
