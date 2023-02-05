package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.request.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.response.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;

import java.util.List;

public interface TodoItemService {
    TodoItemDto addNewTodoItem(String email, TodoItemRequestDto todoItemCreation);

    TodoItem getTodoItem(String email, Long itemId);

    TodoItemDto getTodoItemDto(String email, Long itemId);

    List<TodoItemDto> getAllUsersTodoItems(String email);

    //edit content and priority
    TodoItemDto updateTodoItem(String email, Long itemId, TodoItemRequestDto itemDto);

    //toggle complete
    TodoItemDto toggleComplete(String email, Long itemId);

    //delete
    void deleteItem(String email, Long itemId);

    //get list of available priorities
    List<String> getPriorities();
}
