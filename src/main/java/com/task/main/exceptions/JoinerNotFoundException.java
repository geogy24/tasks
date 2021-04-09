package com.task.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when joiner is not found
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Joiner Not Found")
public class JoinerNotFoundException extends Exception {

}