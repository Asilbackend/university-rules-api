package uz.tuit.unirules.security.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private boolean success;
    private String token;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
