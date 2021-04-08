package com.task.main.repositories;

import com.task.main.models.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query(value = "SELECT t.id FROM tasks t", nativeQuery = true)
    Optional<Long[]> listAllTasksIds();
}
