package edu.tcu.cs.hogwarts_artifact_online.system.exception;

import edu.tcu.cs.hogwarts_artifact_online.system.Result;
import edu.tcu.cs.hogwarts_artifact_online.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// In the class below you can handle multiple exceptions
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)// it is used to tell spring that the method is an exception handler, and you have to add a value to it,you pass the class that is being handled
    @ResponseStatus(HttpStatus.NOT_FOUND)//shows the status of the method
    Result handleObjectNotFoundException (ObjectNotFoundException ex){
        return new Result(false, StatusCode.NOT_FOUND,ex.getMessage(),null);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidation(MethodArgumentNotValidException ex){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String,String> map = new HashMap<>(errors.size());
        errors.forEach((error)->{
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key,val);
        });

        return new Result(false, StatusCode.INVALID_ARGUMENT,"Provided Arguments provided are invalid, see data for details",map);
    }

}
