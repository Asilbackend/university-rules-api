package uz.tuit.unirules.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    private int statusCode;
    private String message;
    private boolean success;
    private T data;
}
