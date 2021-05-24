package com.task.main.repositories;

import com.task.main.models.Task;
import com.task.main.repositories.dtos.DaysLeftToCompleteTaskDto;
import com.task.main.repositories.dtos.TaskCompletedByStackAndJoinerDto;
import com.task.main.repositories.dtos.TaskDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT t.id FROM tasks.tasks t", nativeQuery = true)
    Optional<Long[]> listAllTasksIds();

    @Query(value =
            "SELECT t.joiner_id, (100 * sum(t.worked_hours))/sum(t.estimated_required_hours) AS completed " +
            "FROM tasks.tasks t " +
            "WHERE t.stack = :stackId " +
            "GROUP BY t.joiner_id " +
            "ORDER BY completed DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<TaskCompletedByStackAndJoinerDto> tasksFilteredByStackGroupedByJoinerAndCompleteTask(
            @Param("stackId") Long stackId,
            @Param("limit") Long limit);

    @Query(value =
            "SELECT * " +
            "FROM tasks.tasks t " +
            "ORDER BY joiner_id, (100 * t.worked_hours)/t.estimated_required_hours DESC ",
            nativeQuery = true)
    List<TaskDto> taskCompletedAndUncompletedByJoiner();

    @Query(value =
            "SELECT * " +
            "FROM tasks.tasks t " +
            "WHERE t.joiner_id = :joinerId " +
            "ORDER BY (100 * t.worked_hours)/t.estimated_required_hours DESC ",
            nativeQuery = true)
    List<TaskDto> taskCompletedAndUncompletedFilterByJoiner(@Param("joinerId")  Long joinerId);

    @Query(value =
            "SELECT t.joiner_id, ceil(cast((sum(t.estimated_required_hours) - sum(t.worked_hours)) AS numeric) /8) AS days_left " +
            "FROM tasks.tasks t " +
            "GROUP BY t.joiner_id " +
            "HAVING ceil(cast((sum(t.estimated_required_hours) - sum(t.worked_hours)) AS numeric) /8) > 0 " +
            "ORDER BY joiner_id ", nativeQuery = true)
    List<DaysLeftToCompleteTaskDto> daysLeftToCompleteTaskByJoiner();
}
