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

/**
 * Перехватчик ошибок.
 *
 * @author Anatoliy Pokhresnyi
 */
@ControllerAdvice
public class TreasureExceptionResolver {

    /**
     * Конструктор по умолчанию.
     */
    public TreasureExceptionResolver() {

    }

    /**
     * Преобразовывает ошибку {@link ModificationException} в читаемых формат
     * клиента {@link ExceptionInformation}.
     *
     * @param e Ошибка.
     * @return Ошибка
     */
    @ExceptionHandler(ModificationException.class)
    public ResponseEntity<ExceptionInformation> resolve(
            final ModificationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionInformation(
                                HTTPStatus.BAD_REQUEST, e.getMessage()));
    }

    /**
     * Преобразовывает ошибку {@link NotFoundException} в читаемых формат
     * клиента {@link ExceptionInformation}.
     *
     * @param e Ошибка.
     * @return Ошибка
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionInformation> resolve(
            final NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionInformation(
                                HTTPStatus.NOT_FOUND, e.getMessage()));
    }

    /**
     * Преобразовывает ошибку {@link NotUniqueException} в читаемых формат
     * клиента {@link ExceptionInformation}.
     *
     * @param e Ошибка.
     * @return Ошибка
     */
    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ExceptionInformation> resolve(
            final NotUniqueException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ExceptionInformation(
                                HTTPStatus.INTERNAL_SERVER_ERROR,
                                e.getMessage()));
    }
}
