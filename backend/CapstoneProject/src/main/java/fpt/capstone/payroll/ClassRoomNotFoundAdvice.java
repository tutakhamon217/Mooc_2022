package fpt.capstone.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ClassRoomNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ClassRoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClassRoomNotFound(ClassRoomNotFoundException ex) {
        return ex.getMessage();
    }
}
