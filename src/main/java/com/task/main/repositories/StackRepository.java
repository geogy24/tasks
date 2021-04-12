package com.task.main.repositories;

import com.task.main.models.Stack;
import org.springframework.data.repository.CrudRepository;

public interface StackRepository extends CrudRepository<Stack, Long> {
}
