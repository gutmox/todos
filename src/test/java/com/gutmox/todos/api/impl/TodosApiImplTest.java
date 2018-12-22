package com.gutmox.todos.api.impl;

import com.gutmox.todos.api.TodosApi;
import com.gutmox.todos.api.domain.Todo;
import com.gutmox.todos.api.repositories.mongo.TodoRepositoryMongoImpl;
import com.gutmox.todos.api.validations.TodoValidations;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rx.Single;

import java.util.ArrayList;
import java.util.List;

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

    private List<JsonObject> todoList;

    private JsonObject jsonTodo;

    @Before
    public void setUp() {
        todosApi = new TodosApiImpl(todoRepository, todoValidations);

        todoList = new ArrayList<>();
        jsonTodo = new JsonObject();
        jsonTodo.put("title", "title");
        jsonTodo.put("description", "description");
        jsonTodo.put("priority", 1);
        todo = Todo.fromJson(jsonTodo);
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
                assertThat(res).isEqualTo(uuid);
            });
    }

    @Test
    public void getAll_should_invoke_repository() {

        try {
            todosApi.getAll().doOnError(error -> {}).subscribe(res -> {});
        } catch (Exception ex) {}

        verify(todoRepository).getAll();
    }

    @Test
    public void getAll_should_return_all_values_from_repository() {

        doReturn(Single.just(todoList)).when(todoRepository).getAll();

        todosApi.getAll().doOnError(error -> {}).subscribe(res -> {
            assertThat(res.size()).isEqualTo(0);
        });
    }

    @Test
    public void get_should_invoke_repository() {

        try {
            todosApi.get(uuid).doOnError(error -> {}).subscribe(res -> {});
        } catch (Exception ex) {}

        verify(todoRepository).find(uuid);
    }

    @Test
    public void get_should_return_found_item_from_repository() {

        doReturn(Single.just(jsonTodo)).when(todoRepository).find(uuid);

        todosApi.get(uuid).doOnError(error -> {}).subscribe(res -> {

            assertThat(res.toJson()).isEqualTo(jsonTodo);

        });
    }

    @Test
    public void replace_should_invoke_repository() {

        doReturn(Single.just(true)).when(todoValidations).validate(todo);

        try {
            todosApi.replace(uuid, todo).doOnError(error -> {}).subscribe(res -> {});
        } catch (Exception ex) {}

        verify(todoRepository).replace(uuid, todo.toJson());
    }

    @Test
    public void delete_should_invoke_repository() {

        try {
            todosApi.delete(uuid).doOnError(error -> {}).subscribe(res -> {});
        } catch (Exception ex) {}

        verify(todoRepository).remove(uuid);
    }
}