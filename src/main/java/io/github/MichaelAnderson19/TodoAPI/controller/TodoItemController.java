package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.response.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.dto.request.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.service.TodoItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    /**
     * TODO testing for this and service
     * TODO add exception handling
     * TODO define return response for errors including missing request parameters etc
     */
    private final TodoItemService todoItemService;

    @GetMapping("/{itemId}")
//    @PreAuthorize("hasRole('user')")
    public ResponseEntity<TodoItemDto> getTodoItem(Principal principal, @PathVariable("itemId") Long itemId) {
        TodoItemDto itemDto = todoItemService.getTodoItemDto(principal.getName(), itemId);
        return ResponseEntity.status(HttpStatus.OK).body(itemDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"", "/"})
    public ResponseEntity<List<TodoItemDto>> getAllTodoItemsForUser(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(
                todoItemService.getAllUsersTodoItems(principal.getName())
        );
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TodoItemDto> createTodoItem(Principal principal, @Valid @RequestBody TodoItemRequestDto todoItemRequestDto) {
        TodoItemDto itemDto = todoItemService.addNewTodoItem(principal.getName(), todoItemRequestDto);
        //create uri to created item
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(itemDto.getId().toString()).toUriString());

        return ResponseEntity.status(HttpStatus.CREATED).location(uri).body(itemDto);
    }

    @GetMapping("/toggle/{itemId}")
    public ResponseEntity<TodoItemDto> toggleItemComplete(Principal principal, @PathVariable("itemId") Long itemId) {
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
