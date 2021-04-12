package com.task.main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.task.main.exceptions.*;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
import com.task.main.services.interfaces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = {TaskController.class})
public class TaskControllerTest {

    private final static String TASK_URL = "/api/tasks";
    private final static String GET_TASK_URL = "/api/tasks/{id}";
    private final static String DELETE_TASK_URL = "/api/tasks/{id}";
    private final static String UPDATE_TASK_URL = "/api/tasks/{id}";
    private final static String UTF_8_KEY = "utf-8";
    private final static String STACK_ID_KEY = "stack_id";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskService createTaskService;

    @MockBean
    private ShowTaskIdsService showTaskIdsService;

    @MockBean
    private ShowTaskService showTaskService;

    @MockBean
    private DeleteTaskService deleteTaskService;

    @MockBean
    private UpdateTaskService updateTaskService;

    private Task taskModel;

    private HashMap<String, Object> taskMap;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Faker faker;

    private TaskFactory taskFactory;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.faker = new Faker();

        this.taskFactory = new TaskFactory();
        this.taskMap = taskFactory.map();
        this.taskModel = taskFactory.model(this.taskMap);
    }

    @Test
    public void whenCreatesATaskThenReturnsTaskCreated() throws Exception {
        given(this.createTaskService.execute(any())).willReturn(this.taskModel);

        this.mockMvc
            .perform(post(TASK_URL)
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding(UTF_8_KEY)
                         .content(this.objectMapper.writeValueAsString(this.taskMap)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenCreatesATaskButRoleNotFoundThenReturnsNotFoundException()
        throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(RoleNotFoundException.class);

        this.mockMvc
            .perform(post(TASK_URL)
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding(UTF_8_KEY)
                         .content(this.objectMapper.writeValueAsString(this.taskMap)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenCreatesATaskButStackNotFoundThenReturnsNotFoundException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(StackNotFoundException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenCreatesASubTaskButParentTaskNotFoundThenReturnsNotFoundException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(TaskNotFoundException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenCreatesASubTaskButParentTaskIsNotAParentTaskFoundThenReturnsBadRequestException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(ChildTaskMustNotBeParentTaskException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = java.lang.AssertionError.class)
    public void whenCreatesATaskButSendInvalidDataThenReturnsNotFoundException()
            throws Exception {
        HashMap<String, Object> map = this.taskMap;
        map.put(STACK_ID_KEY, 0);
        when(this.createTaskService.execute(any())).thenThrow(HttpClientErrorException.BadRequest.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = AssertionError.class)
    public void whenCreatesATaskButStackNotFoundThenReturnsInternalServerException()
            throws Exception {
        HashMap<String, Object> map = this.taskMap;
        map.remove(STACK_ID_KEY);
        when(this.createTaskService.execute(any())).thenThrow(HttpServerErrorException.InternalServerError.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(map)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenCreatesATaskButJoinerNotFoundThenReturnsNotFoundException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(JoinerNotFoundException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenCreatesATaskButJoinerHasNotRequiredRoleThenReturnsJoinerHasNotValidRoleException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(JoinerHasNotValidRoleException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenGetAllTaskIdsThenReturnsTasksIds() throws Exception {
        when(this.showTaskIdsService.execute()).thenReturn(new Long[]{1L, 2L});

        this.mockMvc
                .perform(get(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenGetATaskThenReturnsTheTask() throws Exception {
        Long id = Long.valueOf(faker.number().digits(2));
        when(this.showTaskService.execute(anyLong())).thenReturn(this.taskFactory.model());

        this.mockMvc
                .perform(get(GET_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenGetATaskButTaskNotFoundThenRaiseTaskNotFoundException()
            throws Exception {
        Long id = Long.valueOf(faker.number().digits(2));
        when(this.createTaskService.execute(any())).thenThrow(TaskNotFoundException.class);

        this.mockMvc
                .perform(get(GET_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenDeleteATaskThenTaskIsDeleted() throws Exception {
        Long id = Long.valueOf(faker.number().digits(2));
        doNothing().when(this.deleteTaskService).execute(anyLong());

        this.mockMvc
                .perform(delete(DELETE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY))
                .andExpect(status().isOk());
    }

    @Test(expected = MockitoException.class)
    public void whenDeleteATaskButTaskNotFoundThenRaiseTaskNotFoundException()
            throws Exception {
        Long id = Long.valueOf(faker.number().digits(2));
        doThrow(TaskNotFoundException.class).when(this.deleteTaskService).execute(anyLong());

        this.mockMvc
                .perform(delete(DELETE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenUpdatesATaskThenReturnsNoContent() throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.createTaskService.execute(any())).willReturn(this.taskModel);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNoContent());
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesATaskButRoleNotFoundThenReturnsNotFoundException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        when(this.createTaskService.execute(any())).thenThrow(RoleNotFoundException.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesATaskButStackNotFoundThenReturnsNotFoundException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        when(this.createTaskService.execute(any())).thenThrow(StackNotFoundException.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesASubTaskButParentTaskNotFoundThenReturnsNotFoundException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        when(this.createTaskService.execute(any())).thenThrow(TaskNotFoundException.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesASubTaskButParentTaskIsNotAParentTaskFoundThenReturnsBadRequestException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        when(this.createTaskService.execute(any())).thenThrow(ChildTaskMustNotBeParentTaskException.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesAParentTaskButParentTaskCanNotBeAChildTaskFoundThenReturnsBadRequestException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        when(this.createTaskService.execute(any())).thenThrow(ParentTaskMustNotBeChildTaskException.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = java.lang.AssertionError.class)
    public void whenUpdatesATaskButSendInvalidDataThenReturnsNotFoundException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        HashMap<String, Object> map = this.taskMap;
        map.put(STACK_ID_KEY, 0);
        when(this.createTaskService.execute(any())).thenThrow(HttpClientErrorException.BadRequest.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = AssertionError.class)
    public void whenUpdatesATaskButStackNotFoundThenReturnsInternalServerException()
            throws Exception {
        Long id = Long.parseLong(faker.number().digits(3));
        HashMap<String, Object> map = this.taskMap;
        map.remove(STACK_ID_KEY);
        when(this.createTaskService.execute(any())).thenThrow(HttpServerErrorException.InternalServerError.class);

        this.mockMvc
                .perform(put(UPDATE_TASK_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(map)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesATaskButJoinerNotFoundThenReturnsNotFoundException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(JoinerNotFoundException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = MockitoException.class)
    public void whenUpdatesATaskButJoinerHasNotRequiredRoleThenReturnsJoinerHasNotValidRoleException()
            throws Exception {
        when(this.createTaskService.execute(any())).thenThrow(JoinerHasNotValidRoleException.class);

        this.mockMvc
                .perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8_KEY)
                        .content(this.objectMapper.writeValueAsString(this.taskMap)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}