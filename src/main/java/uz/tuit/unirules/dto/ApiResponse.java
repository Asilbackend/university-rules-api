package uz.tuit.unirules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private int statusCode;
    private String message;
    private boolean success;
    private T data;


}
