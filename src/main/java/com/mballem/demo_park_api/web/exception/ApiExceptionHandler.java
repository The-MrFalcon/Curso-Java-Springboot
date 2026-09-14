package com.mballem.demo_park_api.web.exception;

import com.mballem.demo_park_api.exception.*;
import com.mballem.demo_park_api.service.VagaDisponivelException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException ex, HttpServletRequest request){

        Object[] params = new Object[]{ex.getRecurso(),ex.getCodigo()};
        String message = messageSource.getMessage("exception.entityNotFoundException",params,request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request,HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler(CodigoUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> codigoUniqueViolationException(CodigoUniqueViolationException ex, HttpServletRequest request){
        Object[] params = new Object[]{ex.getRecurso(),ex.getCodigo()};
        String message = messageSource.getMessage("exception.codigoUniqueViolationException",params,request.getLocale());
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request,HttpStatus.CONFLICT, message));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex, HttpServletRequest request){

        log.error("Api Error -",ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request,HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> PasswordInvalidException(RuntimeException ex, HttpServletRequest request){

        log.error("Api Error -",ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request,HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(VagaDisponivelException.class)
    public ResponseEntity<ErrorMessage> vagaDisponivelException(RuntimeException ex, HttpServletRequest request){

        log.error("Api Error -",ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request,HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class})
    public ResponseEntity<ErrorMessage> uniqueViolationException(RuntimeException ex, HttpServletRequest request){

        log.error("Api Error -",ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ErrorMessage(request,HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result){

        log.error("Api Error -",ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request,HttpStatus.UNPROCESSABLE_CONTENT, messageSource.getMessage("message.invalid.field",null,request.getLocale()),result,messageSource));
    }

    // Para @PathVariable/ @RequestParam
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolationException(ConstraintViolationException ex, HttpServletRequest request){

        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;

        String field = "";
        String message = "";

        // Extraímos os erros diretamente das "violações"
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()){
            String path = violation.getPropertyPath().toString();
            // Limpa o nome do metodo do caminho(ex: getByCodigo.codigo -> codigo)
            field = path.contains(".") ? path.substring(path.lastIndexOf(".") +1): path;
            message = violation.getMessage();
        }
        return ResponseEntity.status(status).body(new ErrorMessage(request,status,field,message));
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorMessage> noHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request,HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()
        );
        log.error("Resource not found {} {}",error,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request,HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()
        );
        log.error("Internal Server Error {} {}",error,ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> internalServerErrorException(Exception ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request,HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
        log.error("Internal Server Error {} {}",error,ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(error);
    }
}
