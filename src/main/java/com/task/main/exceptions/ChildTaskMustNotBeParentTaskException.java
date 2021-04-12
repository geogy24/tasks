package com.task.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when child task is set as a parent task
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Child task must not be parent task")
public class ChildTaskMustNotBeParentTaskException extends Exception {

}