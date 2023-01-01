package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.dto.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.service.TodoItemService;
import io.github.MichaelAnderson19.TodoAPI.shared.ItemPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public ResponseEntity<TodoItemDto> getTodoItem(Principal principal, @PathVariable("itemId") Long itemId) {
        TodoItemDto itemDto = todoItemService.getTodoItemDto(principal.getName(),itemId);
        return ResponseEntity.ok().body(itemDto);
    }

    @GetMapping("/priorities")
    public ResponseEntity<List<String>> getAllPriorities(){
        return ResponseEntity.ok().body(
                todoItemService.getPriorities()
        );
    }

    @GetMapping({"","/"})
    public ResponseEntity<List<TodoItemDto>> getAllTodoItemsForUser(Principal principal) {
        return ResponseEntity.ok().body(
                todoItemService.getAllUsersTodoItems(principal.getName())
        );
    }

    @PostMapping({"","/"})
    public ResponseEntity<TodoItemDto> createTodoItem(Principal principal, @RequestBody TodoItemRequestDto todoItemRequestDto) {
        TodoItemDto itemDto = todoItemService.addNewTodoItem(principal.getName(), todoItemRequestDto);
        System.out.println("\n\n\n\n\n\n"+todoItemRequestDto.toString()+"\n\n\n\n");
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(itemDto.getId().toString()).toUriString());
        return ResponseEntity.created(uri).body(itemDto);
    }

    @GetMapping("/toggle/{itemId}")
    public ResponseEntity<TodoItemDto> toggleItemComplete(Principal principal,@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok().body(
                todoItemService.toggleComplete(principal.getName(), itemId)
        );
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<TodoItemDto> updateTodoItem(Principal principal, @PathVariable("itemId") Long itemId, @RequestBody TodoItemRequestDto todoItemRequestDto) {
        return ResponseEntity.ok().body(
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
        return ResponseEntity.noContent().build();
    }
}
