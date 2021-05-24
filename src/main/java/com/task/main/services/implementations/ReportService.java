package com.task.main.services.implementations;

import com.task.main.repositories.TaskRepository;
import com.task.main.repositories.dtos.DaysLeftToCompleteTaskDto;
import com.task.main.repositories.dtos.TaskCompletedByStackAndJoinerDto;
import com.task.main.repositories.dtos.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReportService implements com.task.main.services.interfaces.ReportService {
    private final TaskRepository taskRepository;

    @Autowired
    public ReportService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskCompletedByStackAndJoinerDto> tasksFilteredByStackGroupedByJoinerAndCompleteTask(Long stackId, Long quantityRecords) {
        log.info("Get {} task filtered by stack id {}", quantityRecords, stackId);
        return this.taskRepository.tasksFilteredByStackGroupedByJoinerAndCompleteTask(stackId, quantityRecords);
    }

    @Override
    public List<TaskDto> taskCompletedAndUncompletedByJoiner() {
        log.info("Get completed and uncompleted tasks from all joiners");
        return this.taskRepository.taskCompletedAndUncompletedByJoiner();
    }

    @Override
    public List<TaskDto> taskCompletedAndUncompletedFilterByJoiner(Long joinerId) {
        log.info("Get completed and uncompleted tasks filter by joiner");
        return this.taskRepository.taskCompletedAndUncompletedFilterByJoiner(joinerId);
    }

    @Override
    public List<DaysLeftToCompleteTaskDto> daysLeftToCompleteTaskByJoiner() {
        log.info("Days left to complete task by joiner");
        return this.taskRepository.daysLeftToCompleteTaskByJoiner();
    }
}
