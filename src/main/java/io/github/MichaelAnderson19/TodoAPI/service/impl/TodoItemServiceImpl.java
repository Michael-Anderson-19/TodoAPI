package io.github.MichaelAnderson19.TodoAPI.service.impl;

import io.github.MichaelAnderson19.TodoAPI.dto.request.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.response.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.exception.TodoItemNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.TodoItemRepository;
import io.github.MichaelAnderson19.TodoAPI.service.TodoItemService;
import io.github.MichaelAnderson19.TodoAPI.service.impl.UserServiceImpl;
import io.github.MichaelAnderson19.TodoAPI.shared.ItemPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO TEST
@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemRepository todoItemRepository;

    private final UserServiceImpl userService;

    @Override
    public TodoItemDto addNewTodoItem(String email, TodoItemRequestDto todoItemCreation) {
        User user = userService.getUser(email);
        TodoItem newItem = todoItemRepository.save(
                TodoItem.builder()
                        .content(todoItemCreation.getContent())
                        .priority(ItemPriority.valueOf(todoItemCreation.getPriority()))
                        .user(user)
                        .build()
        );
        return TodoItemDto.mapToDto(newItem);
    }

    @Override
    public TodoItem getTodoItem(String email, Long itemId) { //TODO could mark as private
        return todoItemRepository.findByIdAndUserEmail(itemId, email)
                .orElseThrow(() -> new TodoItemNotFoundException(
                        String.format("Error: Todo Item not found")
                ));
    }

    @Override
    public TodoItemDto getTodoItemDto(String email, Long itemId) {
        return TodoItemDto.mapToDto(
                getTodoItem(email, itemId)
        );
    }

    @Override
    public List<TodoItemDto> getAllUsersTodoItems(String email) {
        return todoItemRepository.findAllByUserEmail(email)
                .stream()
                .map(TodoItemDto::mapToDto)
                .collect(Collectors.toList());
    }

    //edit content and priority
    @Override
    public TodoItemDto updateTodoItem(String email, Long itemId, TodoItemRequestDto itemDto) { //TODO needs its own dto??
        TodoItem item = getTodoItem(email, itemId);
        item.setContent(itemDto.getContent());
        item.setPriority(ItemPriority.valueOf(itemDto.getPriority()));

        return TodoItemDto.mapToDto(
                todoItemRepository.save(item));
    }

    //toggle complete
    @Override
    public TodoItemDto toggleComplete(String email, Long itemId) {
        TodoItem item = getTodoItem(email, itemId);
        item.setComplete(!item.isComplete());

        return TodoItemDto.mapToDto(
                todoItemRepository.save(item)
        );
    }

    //delete
    @Override
    public void deleteItem(String email, Long itemId) {
        TodoItem item = getTodoItem(email, itemId);
        todoItemRepository.delete(item);
    }

    //get list of available priorities
    @Override
    public List<String> getPriorities() {
        return Arrays.stream(ItemPriority.values()).map(ItemPriority::toString).collect(Collectors.toList());
    }
}
