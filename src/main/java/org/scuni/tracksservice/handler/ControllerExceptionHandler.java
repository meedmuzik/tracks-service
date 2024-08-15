package org.scuni.tracksservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.scuni.tracksservice.exception.ImageDownloadException;
import org.scuni.tracksservice.exception.ImageUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


@Slf4j
@RestControllerAdvice(basePackages = "org.scuni.tracksserivce.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<Object> handleImageUploadException(ImageUploadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(ImageDownloadException.class)
    public ResponseEntity<Object> handleImageDownloadException(ImageDownloadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Failed to return response: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "problems occurred"));
    }

}
