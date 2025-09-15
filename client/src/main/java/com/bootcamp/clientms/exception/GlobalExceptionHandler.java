package com.bootcamp.clientms.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
    ApiError e = new ApiError();
    e.setStatus(HttpStatus.NOT_FOUND.value());
    e.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
    e.setMessage(ex.getMessage());
    e.setPath(req.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
    ApiError e = new ApiError();
    e.setStatus(HttpStatus.BAD_REQUEST.value());
    e.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
    e.setMessage(ex.getMessage());
    e.setPath(req.getRequestURI());
    return ResponseEntity.badRequest().body(e);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .findFirst().map(fe -> fe.getField() + " " + fe.getDefaultMessage())
        .orElse("Validation error");
    ApiError e = new ApiError();
    e.setStatus(HttpStatus.BAD_REQUEST.value());
    e.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
    e.setMessage(msg);
    e.setPath(req.getRequestURI());
    return ResponseEntity.badRequest().body(e);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
    ApiError e = new ApiError();
    e.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    e.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    e.setMessage(ex.getMessage());
    e.setPath(req.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
  }
}
