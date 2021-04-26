package com.task.main.controllers;

import com.task.main.repositories.dtos.DaysLeftToCompleteTaskDto;
import com.task.main.repositories.dtos.TaskCompletedByStackAndJoinerDto;
import com.task.main.repositories.dtos.TaskDto;
import com.task.main.services.interfaces.ReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Entry point to reports routes
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/reports")
@Log4j2
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/stacks")
    @ApiOperation(value = "Return n task filtered by Stack id, grouped by Joiner and completed task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns tasks"),
    })
    public List<TaskCompletedByStackAndJoinerDto> tasksFilteredByStackGroupedByJoinerAndCompleteTask(
            @RequestParam(name = "stack_id") Long stackId,
            @RequestParam(name = "quantity_records", defaultValue = "10") Long quantityRecords
    ) {
        log.info("Tasks filtered by stack grouped by joiner and complete task");
        return this.reportService.tasksFilteredByStackGroupedByJoinerAndCompleteTask(stackId, quantityRecords);
    }

    @GetMapping("/tasks")
    @ApiOperation(value = "Return task completed and uncompleted by joiner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns task completed and uncompleted by joiner"),
    })
    public List<TaskDto> taskCompletedAndUncompletedByJoiner() {
        log.info("Return tasks order by completeness and joiner");
        return this.reportService.taskCompletedAndUncompletedByJoiner();
    }

    @GetMapping("/joiners/{joinerId}/tasks")
    @ApiOperation(value = "Return task completed and uncompleted by joiner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns task completed and uncompleted by joiner"),
    })
    public List<TaskDto> taskCompletedAndUncompletedFilterByJoiner(@PathVariable Long joinerId) {
        log.info("Tasks filtered by joiner id");
        return this.reportService.taskCompletedAndUncompletedFilterByJoiner(joinerId);
    }

    @GetMapping("/days")
    @ApiOperation(value = "Return task completed and uncompleted by joiner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns task completed and uncompleted by joiner"),
    })
    public List<DaysLeftToCompleteTaskDto> daysLeftToCompleteTaskByJoiner() {
        log.info("Days left to complete task by joiner start");
        return this.reportService.daysLeftToCompleteTaskByJoiner();
    }
}
