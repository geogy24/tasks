package com.task.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when parent task is set as a child task
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Parent task must not be child task")
public class ParentTaskMustNotBeChildTaskException extends Exception {

}