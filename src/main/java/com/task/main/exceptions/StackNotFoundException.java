package com.task.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when stack is not found
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Stack Not Found")
public class StackNotFoundException extends Exception {

}