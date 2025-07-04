package uz.tuit.unirules.handler;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.handler.exceptions.AlreadyExist;
import uz.tuit.unirules.handler.exceptions.ExamNotStartedException;
import uz.tuit.unirules.handler.exceptions.JustFinishedExam;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.http.ResponseEntity;


@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, false, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalExceptions(Exception ex) {
        ex.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Noma'lum xatolik: " + ex.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(BadCredentialsException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Token bilan bog'liq muammo", false, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.CONFLICT.value(), "Ma'lumotlar mos emas yoki allaqachon mavjud", false, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Sizda bu amalni bajarishga ruxsat yo'q", false, null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<?>> handleNoSuchElementException(NoSuchElementException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Element topilmadi: " + e.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AlreadyExist.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyExistException(AlreadyExist e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(JustFinishedExam.class)
    public ResponseEntity<ApiResponse<?>> handleJustFinishedExam(JustFinishedExam ex) {
        ex.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.GONE.value(), "Imtihon allaqachon yakunlangan", false, null);
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }

    @ExceptionHandler(ExamNotStartedException.class)
    public ResponseEntity<ApiResponse<?>> handleExamNotStarted(ExamNotStartedException ex) {
        ex.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.LOCKED.value(), "Imtihon hali boshlanmagan", false, null);
        return ResponseEntity.status(HttpStatus.LOCKED).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(EntityNotFoundException e) {
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Entity topilmadi: " + e.getMessage(), false, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
