package fpt.capstone.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class TooLargeFileAdvice {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleTooLargeFile(MaxUploadSizeExceededException ex)
    {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File is too large");
    }
}
