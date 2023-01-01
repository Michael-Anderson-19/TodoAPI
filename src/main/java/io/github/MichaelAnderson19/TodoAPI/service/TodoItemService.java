package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.TodoItemCreationDto;
import io.github.MichaelAnderson19.TodoAPI.dto.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.exception.TodoItemNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.TodoItemRepository;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final UserService userService;

    public TodoItemDto addNewTodoItem(String email, TodoItemCreationDto todoItemCreation) {
        User user = userService.getUser(email);
        TodoItem newItem = todoItemRepository.save(
                TodoItem.builder()
                        .content(todoItemCreation.getContent())
                        .user(user)
                        .build()
        );
        return TodoItemDto.builder().id(newItem.getId()).userEmail(email).content(newItem.getContent()).build(); //TODO want to return this???
    }

    public TodoItemDto getTodoItem(String email, Long itemId) {
        User user = userService.getUser(email);
        TodoItem item = todoItemRepository.findByIdAndUser(itemId, user)
                .orElseThrow( ()->new TodoItemNotFoundException(
                        String.format("Error: Todo Item not found")
                ));
        return TodoItemDto.mapToDto(item);
    }

    public List<TodoItemDto> getAllUsersTodoItems(String email) {
        User user  = userService.getUser(email);
        return user.getItems()
                .stream()
                .map(TodoItemDto::mapToDto)
                .collect(Collectors.toList());
    }

    //edit content and priority

    //toggle complete

    //delete

}
