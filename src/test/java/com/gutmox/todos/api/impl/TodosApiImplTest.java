package com.gutmox.todos.api.impl;

import com.gutmox.todos.api.TodosApi;
import com.gutmox.todos.api.domain.Todo;
import com.gutmox.todos.api.repositories.mongo.TodoRepositoryMongoImpl;
import com.gutmox.todos.api.validations.TodoValidations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rx.Single;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TodosApiImplTest {

    private TodosApi todosApi;

    @Mock
    private TodoRepositoryMongoImpl todoRepository;

    @Mock
    private TodoValidations todoValidations;

    private Todo todo;

    private String uuid = "uuid";
    @Before
    public void setUp() {
        todosApi = new TodosApiImpl(todoRepository, todoValidations);

        todo = todo.builder().withDescription("description").withPriority(1).withTitle("title").build();
    }

    @Test
    public void save_should_invoke_repository() {

        doReturn(Single.just(true)).when(todoValidations).validate(todo);

        try {
            todosApi.create(todo).doOnError(error -> {}).subscribe(res -> {});
        } catch (Exception ex) {}

        verify(todoRepository).save(todo.toJson());
    }

    @Test
    public void save_should_return_identifier() {

        doReturn(Single.just(true)).when(todoValidations).validate(todo);

        doReturn(Single.just(uuid)).when(todoRepository).save(todo.toJson());

            todosApi.create(todo).doOnError(error -> {}).subscribe(res -> {
                assertThat(uuid).isEqualTo(res);
            });

    }
}