package com.task.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when joiner has not valid role
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Joiner has not valid role")
public class JoinerHasNotValidRoleException extends Exception {

}