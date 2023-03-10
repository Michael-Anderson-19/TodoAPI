package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.request.TodoItemRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.response.TodoItemDto;
import io.github.MichaelAnderson19.TodoAPI.service.impl.TodoItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoItemControllerTest {

    @Mock
    private TodoItemServiceImpl todoItemService;
    @Mock
    private Principal principal;
    private TodoItemController todoItemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        todoItemController = new TodoItemController(todoItemService);
    }

    @Test
    @DisplayName("Given an item Id When an item with that id exists in the database and the getTodoItem method is called Then return the item")
    public void getTodoItemsTest_getItem() {
        final String testEmail = "test@test.com";
        final Long officeId = 1L;
        TodoItemDto itemDto = mock(TodoItemDto.class);

        when(principal.getName()).thenReturn(testEmail);
        when(todoItemService.getTodoItemDto(testEmail, officeId)).thenReturn(itemDto);

        ResponseEntity<TodoItemDto> result = todoItemController.getTodoItem(principal, officeId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(itemDto);

        verify(principal, times(1)).getName();
        verify(todoItemService, times(1)).getTodoItemDto(testEmail, officeId);
    }

    @Test
    @DisplayName("Given a security principal When getAllTodoItemsForUser method is called Then return a list of itemDtos")
    public void getAllTodoItemsForUserTest_getList() {
        final String testEmail = "test@test.com";
        TodoItemDto itemDto = mock(TodoItemDto.class);
        List<TodoItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        when(principal.getName()).thenReturn(testEmail);
        when(todoItemService.getAllUsersTodoItems(testEmail, null)).thenReturn(itemDtoList);

        ResponseEntity<List<TodoItemDto>> result = todoItemController.getAllTodoItemsForUser(principal, null);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(itemDtoList);
        assertThat(result.getBody().size()).isEqualTo(1);

        verify(principal, times(1)).getName();
        verify(todoItemService, times(1)).getAllUsersTodoItems(testEmail, null);
    }

    //@Test //https://stackoverflow.com/questions/9419606/unit-testing-a-method-dependent-to-the-request-context
    @DisplayName("Given a TodoItemRequestDto When createTodoItem method is called Then createItem and return status 201 ")
    public void createTodoItem_createItem() {
        final String testEmail = "test@test.com";
        //URI uri = mock(URI.class);
        TodoItemRequestDto creationDto = TodoItemRequestDto.builder()
                .complete(false)
                .content("some todo item content")
                .priority("HIGH")
                .build();

        TodoItemDto itemDto = TodoItemDto.builder()
                .id(1L)
                .content("some todo item content")
                .priority("HIGH")
                .complete(false)
                .userEmail(testEmail)
                .build();

        when(principal.getName()).thenReturn(testEmail);
        when(todoItemService.addNewTodoItem(testEmail, creationDto)).thenReturn(itemDto);

        ResponseEntity<TodoItemDto> result = todoItemController.createTodoItem(principal, creationDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(itemDto);

        verify(principal, times(1)).getName();
        verify(todoItemService, times(1)).addNewTodoItem(testEmail, creationDto);
    }
}
