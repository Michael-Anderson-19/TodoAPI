package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.response.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.dto.request.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.service.TodoItemService;
import io.github.MichaelAnderson19.TodoAPI.shared.ItemPriority;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoItemController {

    private final TodoItemService todoItemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<TodoItemDto> getTodoItem(Principal principal, @PathVariable("itemId") Long itemId) {
        TodoItemDto itemDto = todoItemService.getTodoItemDto(principal.getName(), itemId);
        return ResponseEntity.status(HttpStatus.OK).body(itemDto);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<TodoItemDto>> getAllTodoItemsForUser(
            Principal principal,
            @RequestParam(name = "priority", required = false) ItemPriority priority,
            @RequestParam(name = "complete", required = false) Boolean complete) {

        List<TodoItemDto> items = todoItemService.getAllUsersTodoItems(principal.getName(), priority, complete);

        HttpStatus status = HttpStatus.OK;

        if (items.size() == 0) status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).body(items
        );
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TodoItemDto> createTodoItem(Principal principal, @Valid @RequestBody TodoItemRequestDto todoItemRequestDto) {
        TodoItemDto itemDto = todoItemService.addNewTodoItem(principal.getName(), todoItemRequestDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(itemDto.getId().toString()).toUriString());

        return ResponseEntity.status(HttpStatus.CREATED).location(uri).body(itemDto);
    }

    @PatchMapping("/toggle/{itemId}")
    public ResponseEntity<TodoItemDto> toggleItemComplete(Principal principal, @PathVariable("itemId") Long itemId) {
        //TODO IMPLEMENT
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<TodoItemDto> updateTodoItem(Principal principal,
                                                      @PathVariable("itemId") Long itemId,
                                                      @Valid @RequestBody TodoItemRequestDto todoItemRequestDto) {

        return ResponseEntity.status(HttpStatus.OK).body(
                todoItemService.updateTodoItem(
                        principal.getName(),
                        itemId,
                        todoItemRequestDto
                )
        );
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteTodoItem(Principal principal, @PathVariable("itemId") Long itemId) {
        todoItemService.deleteItem(principal.getName(), itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/priorities")
    public ResponseEntity<List<String>> getAllPriorities() {
        return ResponseEntity.status(HttpStatus.OK).body(
                todoItemService.getPriorities()
        );
    }
}
