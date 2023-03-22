package io.github.MichaelAnderson19.TodoAPI.service.impl;

import io.github.MichaelAnderson19.TodoAPI.dto.request.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.response.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.exception.TodoItemNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.TodoItemRepository;
import io.github.MichaelAnderson19.TodoAPI.service.TodoItemService;
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
    public TodoItem getTodoItem(String email, Long itemId) {
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
    public List<TodoItemDto> getAllUsersTodoItems(String email, ItemPriority priority, Boolean complete) {

        List<TodoItem> items;

        if (priority != null && complete == null) {
            items = getAllUserItemsByPriority(email, priority);
        } else if (priority == null && complete != null) {
            items = getAllUserItemsByComplete(email, complete);
        } else if (priority != null && complete != null) {
            items = getAllUserItemsByPriorityAndComplete(email, priority, complete);
        } else {
            items = todoItemRepository.findAllByUserEmail(email);
        }
        return items.stream().map(TodoItemDto::mapToDto).collect(Collectors.toList()); //use object mapper
    }


    private List<TodoItem> getAllUserItemsByPriority(String email, ItemPriority priority) {
        return todoItemRepository.findAllByUserEmailAndPriority(email, priority);
    }

    private List<TodoItem> getAllUserItemsByComplete(String email, boolean complete) {
        return todoItemRepository.findAllByUserEmailAndComplete(email, complete);
    }

    private List<TodoItem> getAllUserItemsByPriorityAndComplete(String email, ItemPriority priority, boolean complete) {
        return todoItemRepository.findAllByUserEmailAndPriorityAndComplete(email, priority, complete);
    }

    @Override
    public TodoItemDto updateTodoItem(String email, Long itemId, TodoItemRequestDto itemDto) {
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
