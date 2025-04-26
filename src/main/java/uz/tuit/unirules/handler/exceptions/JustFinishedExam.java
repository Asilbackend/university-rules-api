package uz.tuit.unirules.handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JustFinishedExam extends RuntimeException {
    public JustFinishedExam(String string) {
        super(string);
    }
}
