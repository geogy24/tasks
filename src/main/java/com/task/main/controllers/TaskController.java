package com.task.main.controllers;

import com.task.main.dtos.TaskDto;
import com.task.main.models.Task;
import com.task.main.services.CreateTaskServiceInterface;
import com.task.main.services.ShowTaskIdsServiceInterface;
import com.task.main.services.ShowTaskServiceInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Entry point to tasks routes
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/tasks")
@Log4j2
public class TaskController {
    private final CreateTaskServiceInterface createTaskServiceInterface;
    private final ShowTaskIdsServiceInterface showTaskIdsServiceInterface;
    private final ShowTaskServiceInterface showTaskServiceInterface;

    @Autowired
    public TaskController(
            CreateTaskServiceInterface createTaskServiceInterface,
            ShowTaskIdsServiceInterface showTaskIdsServiceInterface,
            ShowTaskServiceInterface showTaskServiceInterface) {
        this.createTaskServiceInterface = createTaskServiceInterface;
        this.showTaskIdsServiceInterface = showTaskIdsServiceInterface;
        this.showTaskServiceInterface = showTaskServiceInterface;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Return task created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task created")
    })
    public Task create(@Valid @RequestBody TaskDto taskDto) {
        log.info("Create a task with data {}", taskDto);
        return this.createTaskServiceInterface.execute(taskDto);
    }

    @GetMapping
    @ApiOperation(value = "Return all task ids")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All task ids"),
    })
    public Long[] listIds() {
        log.info("Show all task ids");
        return this.showTaskIdsServiceInterface.execute();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Return task search by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return task search by id"),
            @ApiResponse(code = 404, message = "Task not found")
    })
    public Task show(@PathVariable Long id) {
        log.info("Show task by id {}", id);
        return this.showTaskServiceInterface.execute(id);
    }
}
