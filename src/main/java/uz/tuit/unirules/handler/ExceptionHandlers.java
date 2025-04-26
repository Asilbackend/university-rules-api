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

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, false, null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false, null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGlobalExceptions(Exception ex) {
        ex.printStackTrace();
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Noma'lum xatolik: " + ex.getMessage(), false, null);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), false, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<?> handleBadCredentialsException(BadCredentialsException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), false, null);
    }

    @ExceptionHandler(JwtException.class)
    public ApiResponse<?> handleJwtException(JwtException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Token bilan bog'liq muammo", false, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ApiResponse<>(HttpStatus.CONFLICT.value(), "Ma'lumotlar mos emas yoki allaqachon mavjud", false, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<?> handleAccessDeniedException(AccessDeniedException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Sizda bu amalni bajarishga ruxsat yo'q", false, null);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<?> handleNoSuchElementException(NoSuchElementException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Element topilmadi: " + e.getMessage(), false, null);
    }

    @ExceptionHandler(AlreadyExist.class)
    public ApiResponse<?> handleAlreadyExistException(AlreadyExist e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), false, null);
    }

    @ExceptionHandler(JustFinishedExam.class)
    public ApiResponse<?> handleJustFinishedExam(JustFinishedExam ex) {
        ex.printStackTrace();
        return new ApiResponse<>(HttpStatus.GONE.value(), "Imtihon allaqachon yakunlangan", false, null);
    }

    @ExceptionHandler(ExamNotStartedException.class)
    public ApiResponse<?> handleExamNotStarted(ExamNotStartedException ex) {
        ex.printStackTrace();
        // 425 - Too Early (HTTP standard kod, 421 noto'g'ri edi)
        return new ApiResponse<>(HttpStatus.LOCKED.value(), "Imtihon hali boshlanmagan", false, null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
        e.printStackTrace();
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Entity topilmadi: " + e.getMessage(), false, null);
    }
}
