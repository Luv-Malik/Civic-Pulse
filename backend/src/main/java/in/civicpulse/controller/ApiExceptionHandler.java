package in.civicpulse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException ex) {
    Map<String,String> errors=new LinkedHashMap<>(); ex.getBindingResult().getFieldErrors().forEach(e->errors.put(e.getField(),e.getDefaultMessage()));
    return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now().toString(),"status",400,"error","Validation failed","details",errors));
  }
  @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<Map<String,Object>> status(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("timestamp",Instant.now().toString(),"status",ex.getStatusCode().value(),"error",ex.getReason()==null?"Request failed":ex.getReason()));
  }
  @ExceptionHandler(Exception.class) public ResponseEntity<Map<String,Object>> generic(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("timestamp",Instant.now().toString(),"status",500,"error","Unexpected server error"));
  }
}
