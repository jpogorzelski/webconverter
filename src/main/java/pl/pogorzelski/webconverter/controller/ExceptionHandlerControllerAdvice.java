package pl.pogorzelski.webconverter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pogorzelski.webconverter.exception.FileNotFoundException;
import pl.pogorzelski.webconverter.exception.ForbiddenException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle403AppException(ForbiddenException ex) {
        LOGGER.info("AAAA 403333");
        return "/403";
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle404AppException(FileNotFoundException ex) {
        LOGGER.info("AAAA 404");
        return "/404";
    }

}
