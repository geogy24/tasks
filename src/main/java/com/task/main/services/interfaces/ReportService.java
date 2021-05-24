package com.task.main.services.interfaces;

import com.task.main.repositories.dtos.DaysLeftToCompleteTaskDto;
import com.task.main.repositories.dtos.TaskCompletedByStackAndJoinerDto;
import com.task.main.repositories.dtos.TaskDto;

import java.util.List;

public interface ReportService {
    List<TaskCompletedByStackAndJoinerDto> tasksFilteredByStackGroupedByJoinerAndCompleteTask(Long stackId, Long limit);

    List<TaskDto> taskCompletedAndUncompletedByJoiner();

    List<TaskDto> taskCompletedAndUncompletedFilterByJoiner(Long joinerId);

    List<DaysLeftToCompleteTaskDto> daysLeftToCompleteTaskByJoiner();
}
