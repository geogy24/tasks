package com.task.main.controllers;

import com.task.main.dtos.TaskDto;
import com.task.main.dtos.UpdateTaskDto;
import com.task.main.models.Task;
import com.task.main.services.*;
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
    private final DeleteTaskServiceInterface deleteTaskServiceInterface;
    private final UpdateTaskServiceInterface updateTaskServiceInterface;

    @Autowired
    public TaskController(
            CreateTaskServiceInterface createTaskServiceInterface,
            ShowTaskIdsServiceInterface showTaskIdsServiceInterface,
            ShowTaskServiceInterface showTaskServiceInterface,
            DeleteTaskServiceInterface deleteTaskServiceInterface,
            UpdateTaskServiceInterface updateTaskServiceInterface
    ) {
        this.createTaskServiceInterface = createTaskServiceInterface;
        this.showTaskIdsServiceInterface = showTaskIdsServiceInterface;
        this.showTaskServiceInterface = showTaskServiceInterface;
        this.deleteTaskServiceInterface = deleteTaskServiceInterface;
        this.updateTaskServiceInterface = updateTaskServiceInterface;
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

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a task or sub-task")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Return task search by id"),
            @ApiResponse(code = 404, message = "Task not found")
    })
    public void delete(@PathVariable Long id) {
        log.info("Delete task by id {}", id);
        this.deleteTaskServiceInterface.execute(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a task or sub-task")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Return task search by id"),
            @ApiResponse(code = 404, message = "Task not found")
    })
    public void update(@PathVariable Long id, @RequestBody UpdateTaskDto updateTaskDto) {
        log.info("Delete task by id {} and body {}", id, updateTaskDto);
        this.updateTaskServiceInterface.execute(id, updateTaskDto);
    }
}
