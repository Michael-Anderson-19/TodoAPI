package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.exception.TodoItemNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.TodoItemRepository;
import io.github.MichaelAnderson19.TodoAPI.shared.ItemPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
//TODO TEST
@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;

    private final UserService userService;

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

    public TodoItem getTodoItem(String email, Long itemId) { //TODO could mark as private
         return todoItemRepository.findByIdAndUserEmail(itemId, email)
                 .orElseThrow( ()->new TodoItemNotFoundException(
                        String.format("Error: Todo Item not found")
                ));
    }

    public TodoItemDto getTodoItemDto(String email, Long itemId) {
        return TodoItemDto.mapToDto(
            getTodoItem(email, itemId)
        );
    }

    public List<TodoItemDto> getAllUsersTodoItems(String email) {
        return todoItemRepository.findAllByUserEmail(email)
                .stream()
                .map(TodoItemDto::mapToDto)
                .collect(Collectors.toList());
    }

    //edit content and priority
    public TodoItemDto updateTodoItem(String email, Long itemId, TodoItemRequestDto itemDto){ //TODO needs its own dto??
        TodoItem item = getTodoItem(email, itemId);
        item.setContent(itemDto.getContent());
        item.setPriority(ItemPriority.valueOf(itemDto.getPriority()));

        return TodoItemDto.mapToDto(
                todoItemRepository.save(item));
    }

    //toggle complete
    public TodoItemDto toggleComplete(String email, Long itemId) {
        TodoItem item = getTodoItem(email, itemId);
        item.setComplete(!item.isComplete());

        return TodoItemDto.mapToDto(
            todoItemRepository.save(item)
        );
    }

    //delete
    public void deleteItem(String email, Long itemId){
        TodoItem item = getTodoItem(email,itemId);
        todoItemRepository.delete(item);
    }

    //get list of available priorities
    public List<String> getPriorities() {
       return Arrays.stream(ItemPriority.values()).map(ItemPriority::toString).collect(Collectors.toList());
    }
}
