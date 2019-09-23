package com.github.onotoliy.opposite.treasure;

import com.github.onotoliy.opposite.data.core.ExceptionInformation;
import com.github.onotoliy.opposite.data.core.HTTPStatus;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import com.github.onotoliy.opposite.treasure.exceptions.NotUniqueException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TreasureExceptionResolver {

    @ExceptionHandler(ModificationException.class)
    public ResponseEntity<ExceptionInformation> resolve(ModificationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionInformation(HTTPStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionInformation> resolve(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionInformation(HTTPStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ExceptionInformation> resolve(NotUniqueException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ExceptionInformation(HTTPStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
