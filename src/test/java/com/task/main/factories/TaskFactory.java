package com.task.main.factories;

import com.github.javafaker.Faker;
import com.task.main.dtos.TaskDto;
import com.task.main.dtos.UpdateTaskDto;
import com.task.main.models.Task;

import java.util.HashMap;

public class TaskFactory {
    private final static String NAME_KEY = "name";
    private final static String DESCRIPTION_KEY = "description";
    private final static String ESTIMATED_REQUIRED_HOURS_KEY = "estimated_required_hours";
    private final static String ROLE_IDS_KEY = "role_ids";
    private final static String STACK_ID_KEY = "stack_id";
    private final static String PARENT_TASK_ID_KEY = "parent_task_id";

    private final Faker faker;

    public TaskFactory() {
        this.faker = new Faker();
    }

    public HashMap<String, Object> map() {
        return new HashMap<>() {{
            put(NAME_KEY, faker.lorem().word());
            put(DESCRIPTION_KEY, faker.lorem().sentence());
            put(ESTIMATED_REQUIRED_HOURS_KEY, faker.number().digits(2));
            put(ROLE_IDS_KEY, new Long[]{Long.parseLong(faker.number().digits(3))});
            put(STACK_ID_KEY, faker.number().digits(3));
            put(PARENT_TASK_ID_KEY, faker.number().digits(3));
        }};
    }

    public Task model(HashMap<String, Object> taskMap) {
        return Task.builder()
                .name(taskMap.get(NAME_KEY).toString())
                .description(taskMap.get(DESCRIPTION_KEY).toString())
                .estimatedRequiredHours(Integer.parseInt(taskMap.get(ESTIMATED_REQUIRED_HOURS_KEY).toString()))
                .build();
    }

    public Task model() {
        return Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .estimatedRequiredHours(Integer.valueOf(faker.number().digits(2)))
                .build();
    }

    public TaskDto dto() {
        return TaskDto.builder()
                .name(faker.name().firstName())
                .description(faker.lorem().sentence())
                .estimatedRequiredHours(Integer.valueOf(faker.number().digits(2)))
                .roleIds(new Long[]{Long.parseLong(faker.number().digits(3))})
                .stackId(Long.parseLong(faker.number().digits(3)))
                .parentTaskId(Long.parseLong(faker.number().digits(3)))
                .build();
    }

    public UpdateTaskDto updateDto() {
        return UpdateTaskDto.builder()
                .name(faker.name().firstName())
                .description(faker.lorem().sentence())
                .estimatedRequiredHours(Integer.valueOf(faker.number().digits(2)))
                .roleIds(new Long[]{Long.parseLong(faker.number().digits(3))})
                .stackId(Long.parseLong(faker.number().digits(3)))
                .parentTaskId(Long.parseLong(faker.number().digits(3)))
                .build();
    }
}
